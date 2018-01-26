package stockmarket.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import stockmarket.model.Asset;
import stockmarket.model.Currency;
import stockmarket.model.ExchangeMarket;
import stockmarket.model.Gambler;
import stockmarket.model.Order;
import stockmarket.model.Possession;
import stockmarket.model.RatingRecord;
import stockmarket.model.RawMaterial;
import stockmarket.model.Share;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.RandomDataGenerator;

/**
 * simulation class that is responsible for managing the simulation and all threads
 * @author Damian Horna
 *
 */
public class Simulation implements Runnable {

	/**
	 * list of all assets in the simulation
	 */
	private List<Asset> allAssets;

	/**
	 * adds USD to the simulation
	 */
	private void addDollar() {
		Currency USD = new Currency("USD", Arrays.asList("United States"));
		RandomDataGenerator generator = new RandomDataGenerator();
		RatingRecord ratRec = new RatingRecord(BigDecimal.ONE, new Date(), new BigDecimal(0));

		USD.getRatingRecords().add(ratRec);
		USD.setInitialRate(BigDecimal.ONE);

		USD.setAllTimeHigh(BigDecimal.ONE);
		USD.setAllTimeLow(BigDecimal.ONE);
		USD.setInitialRate(BigDecimal.ONE);
		USD.setMinimalRate(BigDecimal.ONE);
		USD.setCurrentRate(BigDecimal.ONE);
		USD.setMaximalRate(BigDecimal.ONE);
		USD.setChange(BigDecimal.ZERO);
		USD.setChangePct(BigDecimal.ZERO);
		USD.setQuoted(true);
		generator.getCountriesForCurrencies().remove("United States");
		PseudoDB.getCurrencies().add(USD);
	}

	/**
	 * adds first exchange market to the simulation, that dollar is quoted on
	 */
	private void addForex() {
		ExchangeMarket forex = new ExchangeMarket("Forex", "Decentralized", "Decentralized", "Decentralized",
				new BigDecimal("1"));
		forex.getQuotedCurrencies().add(PseudoDB.getCurrencyByName("USD"));
		PseudoDB.getCurrencyByName("USD").setQuoted(true);
		RatingRecord ratingRecord = new RatingRecord(new BigDecimal("1"), new Date(), new BigDecimal(0));
		PseudoDB.getCurrencyByName("USD").getRatingRecords().add(ratingRecord);
		PseudoDB.getExchangeMarkets().add(forex);
	}

	/**
	 * gets the list of all assets
	 */
	private void getAllAssets() {
		try {
			this.allAssets.addAll(PseudoDB.getShares());
			this.allAssets.addAll(PseudoDB.getCurrencies());
			this.allAssets.addAll(PseudoDB.getRawMaterials());

		} catch (NullPointerException ex) {
			//
		}
	}

	/**
	 * initializes the simulation
	 */
	private void initialize() {
		addDollar();
		addForex();
	}

	@Override
	public void run() {
		initialize();
		getAllAssets();
		Thread quotingWriter = new Thread(new QuotingWriter());
		quotingWriter.start();

		Thread indexQuotingWriter = new Thread(new IndexQuotingWriter());
		indexQuotingWriter.start();
		while (true) {
			getAllAssets();
			executeOrders();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * executes all of the orders that came up recently
	 */
	private void executeOrders() {

		List<Order> orders = PseudoDB.getOrders();
		HashMap<Gambler, BigDecimal> buyers;
		HashMap<Gambler, BigDecimal> sellers;
		for (Order o : orders) {
			buyers = o.getBuyers();
			sellers = o.getSellers();
			if (o.getAsset() instanceof Share) {
				synchronized (PseudoDB.sharesLock) {
					Share share = (Share) o.getAsset();
					BigDecimal demand = calculacteDemand(buyers);
					BigDecimal supply = calculateSupply(sellers);
					BigDecimal tempVol = handleShares(buyers, sellers, o);
					BigDecimal newRate = calculateChange(share, tempVol, demand, supply);
					RatingRecord record = new RatingRecord(newRate, new Date(), tempVol);
					share.getRatingRecords().add(record);
					updateAsset(share, newRate);
				}
			} else if (o.getAsset() instanceof Currency) {
				synchronized(PseudoDB.currenciesLock) {
					Currency currency = (Currency) o.getAsset();
					BigDecimal demand = calculacteDemand(buyers);
					BigDecimal supply = calculateSupply(sellers);
					BigDecimal tempVol = handleCurrencies(buyers, sellers, o);
					BigDecimal newRate = calculateChange(currency, tempVol, demand, supply);
					RatingRecord record = new RatingRecord(newRate, new Date(), tempVol);
					currency.getRatingRecords().add(record);
					updateAsset(currency, newRate);
				}
			} else if (o.getAsset() instanceof RawMaterial) {
				synchronized(PseudoDB.rawMaterialsLock) {
					RawMaterial material = (RawMaterial) o.getAsset();
					BigDecimal demand = calculacteDemand(buyers);
					BigDecimal supply = calculateSupply(sellers);
					BigDecimal tempVol = handleRawMaterials(buyers, sellers, o);
					BigDecimal newRate = calculateChange(material, tempVol, demand, supply);
					RatingRecord record = new RatingRecord(newRate, new Date(), tempVol);
					material.getRatingRecords().add(record);
					updateAsset(material, newRate);
				}
			}
		}

	}

	/**
	 * handles orders bound to raw materials
	 * @param buyers list of buyers of specific raw material
	 * @param sellers list of sellers of specific raw material
	 * @param o reference to the order
	 * @return returns volume
	 */
	private BigDecimal handleRawMaterials(HashMap<Gambler, BigDecimal> buyers, HashMap<Gambler, BigDecimal> sellers,
			Order o) {
		RawMaterial material = (RawMaterial) o.getAsset();
		BigDecimal tempVolume = BigDecimal.ZERO;
		BigDecimal avDirectly = material.getQuantity();
		BigDecimal avIndirectly = BigDecimal.ZERO;
		for (Map.Entry<Gambler, BigDecimal> s : sellers.entrySet()) {
			avIndirectly = avIndirectly.add(s.getValue());
		}
		for (Map.Entry<Gambler, BigDecimal> g : buyers.entrySet()) {
			if (g.getValue().compareTo(avDirectly.add(avIndirectly)) < 0) { // buyer wants to buy less than available
				while (g.getValue().compareTo(BigDecimal.ZERO) > 0) {// buyer wants to buy more than 0
					BigDecimal howManyBuyerWants = g.getValue();
					if (sellers.size() > 0) {// are any sellers left?
						Gambler seller = sellers.keySet().stream().findFirst().get(); // get random seller key

						BigDecimal howManySellerWants = sellers.get(seller);
						if (howManySellerWants.compareTo(howManyBuyerWants) > 0) {// s>b
							howManySellerWants = howManySellerWants.subtract(howManyBuyerWants);
							Possession posSeller = seller.getPossessions().stream()
									.filter(p -> p.getAssetType().equals(material)).collect(Collectors.toList()).get(0);
							posSeller.setQuantity(posSeller.getQuantity().subtract(howManyBuyerWants));
							updatePossessions(material, g, howManyBuyerWants);
							updateBudgets(material, g, seller, howManyBuyerWants);
							material.getOwners().add(g.getKey());
							tempVolume = tempVolume.add(howManyBuyerWants);
							g.setValue(BigDecimal.ZERO);
							sellers.put(seller, howManySellerWants);
							avIndirectly = avIndirectly.subtract(howManyBuyerWants);
						} else if (howManySellerWants.compareTo(howManyBuyerWants) <= 0) {// s<=b
							avIndirectly = avIndirectly.subtract(howManySellerWants);
							seller.setPossessions(seller.getPossessions().stream()
									.filter(p -> !p.getAssetType().equals(material)).collect(Collectors.toList()));
							material.getOwners().add(g.getKey());
							material.getOwners().remove(seller);
							updatePossessions(material, g, howManySellerWants);
							updateBudgets(material, g, seller, howManySellerWants);
							tempVolume = tempVolume.add(howManySellerWants);
							howManyBuyerWants = howManyBuyerWants.subtract(howManySellerWants);
							g.setValue(howManyBuyerWants);
							sellers.remove(seller);
						}

					} else {// no sellers left
						// czy liczba akcji jest wystarczajaca
						if (material.getQuantity().compareTo(howManyBuyerWants) >= 0) {
							material.setQuantity(material.getQuantity().subtract(howManyBuyerWants));
							avDirectly = avDirectly.subtract(howManyBuyerWants);
							g.getKey().setBudget(g.getKey().getBudget()
									.subtract(material.getCurrentRate().multiply(howManyBuyerWants)));
							updatePossessions(material, g, howManyBuyerWants);
							if (!material.getOwners().contains(g.getKey())) {
								material.getOwners().add(g.getKey());
							}
							tempVolume = tempVolume.add(howManyBuyerWants);
							buyers.put(g.getKey(), BigDecimal.ZERO);
						}
					}
				}
			}
		}
		for (Iterator<Entry<Gambler, BigDecimal>> it = buyers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Gambler, BigDecimal> g = it.next();
			if (g.getValue().equals(BigDecimal.ZERO)) {
				it.remove();
			}
		}
		return tempVolume;
	}

	/**
	 * executes transactions with currencies
	 * @param buyers list of buyers of specific currency
	 * @param sellers list of sellers of specific currency
	 * @param o order
	 * @return returns volume
	 */
	private BigDecimal handleCurrencies(HashMap<Gambler, BigDecimal> buyers, HashMap<Gambler, BigDecimal> sellers,
			Order o) {
		Currency currency = (Currency) o.getAsset();
		BigDecimal tempVolume = BigDecimal.ZERO;
		BigDecimal avDirectly = currency.getQuantity();
		BigDecimal avIndirectly = BigDecimal.ZERO;
		for (Map.Entry<Gambler, BigDecimal> s : sellers.entrySet()) {
			avIndirectly = avIndirectly.add(s.getValue());
		}
		for (Map.Entry<Gambler, BigDecimal> g : buyers.entrySet()) {
			if (g.getValue().compareTo(avDirectly.add(avIndirectly)) < 0) { // buyer wants to buy less than available
				while (g.getValue().compareTo(BigDecimal.ZERO) > 0) {// buyer wants to buy more than 0
					BigDecimal howManyBuyerWants = g.getValue();
					if (sellers.size() > 0) {// are any sellers left?
						Gambler seller = sellers.keySet().stream().findFirst().get(); // get random seller key

						BigDecimal howManySellerWants = sellers.get(seller);
						if (howManySellerWants.compareTo(howManyBuyerWants) > 0) {// s>b
							howManySellerWants = howManySellerWants.subtract(howManyBuyerWants);
							Possession posSeller = seller.getPossessions().stream()
									.filter(p -> p.getAssetType().equals(currency)).collect(Collectors.toList()).get(0);
							posSeller.setQuantity(posSeller.getQuantity().subtract(howManyBuyerWants));
							updatePossessions(currency, g, howManyBuyerWants);
							updateBudgets(currency, g, seller, howManyBuyerWants);
							currency.getOwners().add(g.getKey());
							tempVolume = tempVolume.add(howManyBuyerWants);
							g.setValue(BigDecimal.ZERO);
							sellers.put(seller, howManySellerWants);
							avIndirectly = avIndirectly.subtract(howManyBuyerWants);
						} else if (howManySellerWants.compareTo(howManyBuyerWants) <= 0) {// s<=b
							avIndirectly = avIndirectly.subtract(howManySellerWants);
							seller.setPossessions(seller.getPossessions().stream()
									.filter(p -> !p.getAssetType().equals(currency)).collect(Collectors.toList()));
							currency.getOwners().add(g.getKey());
							currency.getOwners().remove(seller);
							updatePossessions(currency, g, howManySellerWants);
							updateBudgets(currency, g, seller, howManySellerWants);
							tempVolume = tempVolume.add(howManySellerWants);
							howManyBuyerWants = howManyBuyerWants.subtract(howManySellerWants);
							g.setValue(howManyBuyerWants);
							sellers.remove(seller);
						}

					} else {// no sellers left
						// czy liczba akcji jest wystarczajaca
						if (currency.getQuantity().compareTo(howManyBuyerWants) >= 0) {
							currency.setQuantity(currency.getQuantity().subtract(howManyBuyerWants));
							avDirectly = avDirectly.subtract(howManyBuyerWants);
							g.getKey().setBudget(g.getKey().getBudget()
									.subtract(currency.getCurrentRate().multiply(howManyBuyerWants)));
							updatePossessions(currency, g, howManyBuyerWants);
							if (!currency.getOwners().contains(g.getKey())) {
								currency.getOwners().add(g.getKey());
							}
							tempVolume = tempVolume.add(howManyBuyerWants);
							buyers.put(g.getKey(), BigDecimal.ZERO);
						}
					}
				}
			}
		}
		for (Iterator<Entry<Gambler, BigDecimal>> it = buyers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Gambler, BigDecimal> g = it.next();
			if (g.getValue().equals(BigDecimal.ZERO)) {
				it.remove();
			}
		}
		return tempVolume;
	}

	/**
	 * calculates supply of the asset
	 * @param sellers list of sellers
	 * @return returns big decimal supply
	 */
	private BigDecimal calculateSupply(HashMap<Gambler, BigDecimal> sellers) {
		BigDecimal supply = BigDecimal.ZERO;
		for (Map.Entry<Gambler, BigDecimal> s : sellers.entrySet()) {
			supply = supply.add(s.getValue());
		}
		return supply;
	}

	/**
	 * calculates demand of the asset
	 * @param buyers list of buyers
	 * @return returns big decimal demand
	 */
	private BigDecimal calculacteDemand(HashMap<Gambler, BigDecimal> buyers) {
		BigDecimal demand = BigDecimal.ZERO;
		for (Map.Entry<Gambler, BigDecimal> b : buyers.entrySet()) {
			demand = demand.add(b.getValue());
		}
		return demand;
	}

	/**
	 * executes transaction with shares
	 * @param buyers list of buyers of given shares
	 * @param sellers list of sellers of given share
	 * @param o order
	 * @return returns volume
	 */
	private BigDecimal handleShares(HashMap<Gambler, BigDecimal> buyers, HashMap<Gambler, BigDecimal> sellers,
			Order o) {
		Share share = (Share) o.getAsset();
		BigDecimal tempVolume = BigDecimal.ZERO;
		BigDecimal avDirectly = share.getCompany().getNumOfShares();
		BigDecimal avIndirectly = BigDecimal.ZERO;
		for (Map.Entry<Gambler, BigDecimal> s : sellers.entrySet()) {
			avIndirectly = avIndirectly.add(s.getValue());
		}
		for (Map.Entry<Gambler, BigDecimal> g : buyers.entrySet()) {
			if (g.getValue().compareTo(avDirectly.add(avIndirectly)) < 0) { // buyer wants to buy less than available
				while (g.getValue().compareTo(BigDecimal.ZERO) > 0) {// buyer wants to buy more than 0
					BigDecimal howManyBuyerWants = g.getValue();
					if (sellers.size() > 0) {// are any sellers left?
						Gambler seller = sellers.keySet().stream().findFirst().get(); // get random seller key

						BigDecimal howManySellerWants = sellers.get(seller);
						if (howManySellerWants.compareTo(howManyBuyerWants) > 0) {// s>b
							howManySellerWants = howManySellerWants.subtract(howManyBuyerWants);
							Possession posSeller = seller.getPossessions().stream()
									.filter(p -> p.getAssetType().equals(share)).collect(Collectors.toList()).get(0);
							posSeller.setQuantity(posSeller.getQuantity().subtract(howManyBuyerWants));
							updatePossessions(share, g, howManyBuyerWants);
							updateBudgets(share, g, seller, howManyBuyerWants);
							share.getCompany().getShareholders().add(g.getKey());
							tempVolume = tempVolume.add(howManyBuyerWants);
							g.setValue(BigDecimal.ZERO);
							sellers.put(seller, howManySellerWants);
							avIndirectly = avIndirectly.subtract(howManyBuyerWants);
						} else if (howManySellerWants.compareTo(howManyBuyerWants) <= 0) {// s<=b
							avIndirectly = avIndirectly.subtract(howManySellerWants);
							seller.setPossessions(seller.getPossessions().stream()
									.filter(p -> !p.getAssetType().equals(share)).collect(Collectors.toList()));
							share.getCompany().getShareholders().add(g.getKey());
							share.getCompany().getShareholders().remove(seller);
							updatePossessions(share, g, howManySellerWants);
							updateBudgets(share, g, seller, howManySellerWants);
							tempVolume = tempVolume.add(howManySellerWants);
							howManyBuyerWants = howManyBuyerWants.subtract(howManySellerWants);
							g.setValue(howManyBuyerWants);
							sellers.remove(seller);
						}

					} else {// no sellers left
						// czy liczba akcji jest wystarczajaca
						if (share.getCompany().getNumOfShares().compareTo(howManyBuyerWants) >= 0) {
							share.getCompany()
									.setNumOfShares(share.getCompany().getNumOfShares().subtract(howManyBuyerWants));
							avDirectly = avDirectly.subtract(howManyBuyerWants);
							g.getKey().setBudget(g.getKey().getBudget()
									.subtract(share.getCurrentRate().multiply(howManyBuyerWants)));
							updatePossessions(share, g, howManyBuyerWants);
							if (!share.getCompany().getShareholders().contains(g.getKey())) {
								share.getCompany().getShareholders().add(g.getKey());
							}
							tempVolume = tempVolume.add(howManyBuyerWants);
							buyers.put(g.getKey(), BigDecimal.ZERO);
						}
					}
				}
			}
		}
		for (Iterator<Entry<Gambler, BigDecimal>> it = buyers.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Gambler, BigDecimal> g = it.next();
			if (g.getValue().equals(BigDecimal.ZERO)) {
				it.remove();
			}
		}
		return tempVolume;

	}

	/**
	 * updates the ratings of asset
	 * @param asset asset to be updated
	 * @param newRate new rate
	 */
	private void updateAsset(Asset asset, BigDecimal newRate) {
		if (newRate.compareTo(asset.getAllTimeHigh()) > 0)
			asset.setAllTimeHigh(newRate);
		if (newRate.compareTo(asset.getAllTimeLow()) < 0)
			asset.setAllTimeLow(newRate);
		if (newRate.compareTo(asset.getMinimalRate()) < 0)
			asset.setMinimalRate(newRate);
		if (newRate.compareTo(asset.getMaximalRate()) > 0)
			asset.setMaximalRate(newRate);
		asset.setChange(newRate.subtract(asset.getInitialRate()));
		asset.setChangePct(asset.getChange().divide(asset.getInitialRate(), 2, RoundingMode.HALF_UP));
		asset.setCurrentRate(newRate);
	}

	/**
	 * calculates change comparing to previous session
	 * @param asset asset to deal with
	 * @param tempVolume volume during the execution of transactions
	 * @param demand demand
	 * @param supply supply
	 * @return returns new rate
	 */
	private BigDecimal calculateChange(Asset asset, BigDecimal tempVolume, BigDecimal demand, BigDecimal supply) {
		BigDecimal prevVolume = BigDecimal.ONE;
		if (!(asset.getRatingRecords().size() == 0)) {
			prevVolume = asset.getRatingRecords().get(asset.getRatingRecords().size() - 1).getVolume();
		}

		BigDecimal ratio = BigDecimal.ZERO;
		if (!tempVolume.equals(BigDecimal.ZERO)) {
			ratio = prevVolume.divide(tempVolume, 2, RoundingMode.HALF_UP);
		} else
			ratio = BigDecimal.ONE;

		if (ratio.compareTo(BigDecimal.ONE) < 0) {
			ratio = BigDecimal.ONE;
		}

		BigDecimal change = asset.getCurrentRate().divide(new BigDecimal("100").multiply(ratio), 3,
				RoundingMode.HALF_UP);
		if (demand.compareTo(supply) >= 0) // demand>=supplly
			return asset.getCurrentRate().add(change).add(change);
		else {
			return asset.getCurrentRate().subtract(change);
		}

	}

	/**
	 * updates possessions of the investors
	 * @param asset type of the asset
	 * @param g map entries of all gamblers
	 * @param howManyBuyerWants quantity that buyer wants to buy
	 */
	private void updatePossessions(Asset asset, Map.Entry<Gambler, BigDecimal> g, BigDecimal howManyBuyerWants) {
		try { // if a buyer already has this share then update it
			Possession posBuy = g.getKey().getPossessions().stream().filter(p -> p.getAssetType().equals(asset))
					.collect(Collectors.toList()).get(0);
			posBuy.setQuantity(posBuy.getQuantity().add(howManyBuyerWants));
		} catch (IndexOutOfBoundsException e) {// if he doesn't then add it
			Possession posBuyer = new Possession(asset, howManyBuyerWants);
			g.getKey().getPossessions().add(posBuyer);
		}
	}

	/**
	 * updates budgets of all investors
	 * @param asset type of the asset
	 * @param g map entries of all gamblers
	 * @param seller seller
	 * @param howManyBuyerWants quantity that buyers want to buy
	 */
	private void updateBudgets(Asset asset, Map.Entry<Gambler, BigDecimal> g, Gambler seller,
			BigDecimal howManyBuyerWants) {
		g.getKey().setBudget(g.getKey().getBudget().subtract(asset.getCurrentRate().multiply(howManyBuyerWants)));

		seller.setBudget(seller.getBudget().add(asset.getCurrentRate().multiply(howManyBuyerWants)));
		seller.setOperationalBudget(
				seller.getOperationalBudget().add(asset.getCurrentRate().multiply(howManyBuyerWants)));
	}
}
