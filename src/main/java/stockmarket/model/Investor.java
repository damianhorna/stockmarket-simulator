package stockmarket.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import stockmarket.utils.PseudoDB;

/**
 * investor class
 * @author Damian Horna
 *
 */
public class Investor extends Gambler {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 2221399425229379950L;
	/**
	 * name of the investor
	 */
	private String name;
	/**
	 * surname of the investor
	 */
	private String surname;
	/**
	 * pesel of the investor
	 */
	private String PESEL;
	/**
	 * hash map of investment fund shares
	 */
	private HashMap<InvestmentFundShare, BigDecimal> fundShares;

	public Investor(BigDecimal budget, List<Possession> assets, String name, String surname, String pESEL,
			HashMap<InvestmentFundShare, BigDecimal> fundShares) {
		super(budget, assets);
		this.name = name;
		this.surname = surname;
		PESEL = pESEL;
		this.fundShares = fundShares;
	}

	public Investor(String name, String surname, String PESEL, String budget) {
		this(new BigDecimal(budget), new ArrayList<>(), name, surname, PESEL, new HashMap<>());
	}

	public HashMap<InvestmentFundShare, BigDecimal> getFundShares() {
		return fundShares;
	}

	public String getName() {
		return name;
	}

	public String getPESEL() {
		return PESEL;
	}

	public String getSurname() {
		return surname;
	}

	public void setFundShares(HashMap<InvestmentFundShare, BigDecimal> fundShares) {
		this.fundShares = fundShares;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPESEL(String pESEL) {
		PESEL = pESEL;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		return "Investor [name=" + name + ", surname=" + surname + ", PESEL=" + PESEL + ", fundShares=" + fundShares
				+ ", budget=" + super.getBudget() + ", assets=" + super.getPossessions() + "]";
	}

	@Override
	public void invest() {
		// probability of increasing the budget equal to 1/10
		if ((ThreadLocalRandom.current().nextInt(1, 11) % 7) == 0) {
			increaseTheBudget();
		}

		if ((ThreadLocalRandom.current().nextInt(1, 11) % 5) == 0) {
			tryToBuySomething();
		}

		if ((ThreadLocalRandom.current().nextInt(1, 11) % 5) == 0) {
			tryToSellSomething();
		}
	}

	/**
	 * increases the budget of investor by a random number
	 */
	public void increaseTheBudget() {

		BigDecimal bud = new BigDecimal(ThreadLocalRandom.current().nextInt(1000, 3000));
		this.setBudget(this.getBudget().add(bud));
		this.setOperationalBudget(this.getOperationalBudget().add(bud));

	}

	@Override
	public void run() {
		while (true) {
			invest();
			try {
				Thread.sleep(5000);
				// let it sleep, free the processor, let others do more
			} catch (InterruptedException e) {
				//
				e.printStackTrace();
			}

		}
	}

	/**
	 * responsible for selling assets of investor
	 */
	private void tryToSellSomething() {

		if (this.getPossessions().size() > 0) {
			Possession possession = this.getPossessions()
					.get(ThreadLocalRandom.current().nextInt(0, this.getPossessions().size()));
			if (possession.getAssetType() instanceof Share) {
				synchronized (PseudoDB.sharesLock) {
					BigDecimal howMuch = new BigDecimal(ThreadLocalRandom.current().nextInt(1,
							Integer.valueOf(possession.getQuantity().intValue() + 1)));
					Order ord = new Order();
					try { // meaning sellers already sells it
						ord = PseudoDB.getOrders().stream().filter(o -> o.getAsset() == possession.getAssetType())
								.collect(Collectors.toList()).get(0);
						ord.getSellers().put(this, howMuch);
					} catch (IndexOutOfBoundsException e) { // doesnt sell it already
						ord.setAsset(possession.getAssetType());
						ord.getSellers().put(this, howMuch);
						PseudoDB.getOrders().add(ord);
					}
				}
			} else if (possession.getAssetType() instanceof Currency) {
				synchronized (PseudoDB.currenciesLock) {
					BigDecimal howMuch = new BigDecimal(ThreadLocalRandom.current().nextInt(1,
							Integer.valueOf(possession.getQuantity().intValue() + 1)));
					Order ord = new Order();
					try { // meaning sellers already sells it
						ord = PseudoDB.getOrders().stream().filter(o -> o.getAsset() == possession.getAssetType())
								.collect(Collectors.toList()).get(0);
						ord.getSellers().put(this, howMuch);
					} catch (IndexOutOfBoundsException e) { // doesnt sell it already
						ord.setAsset(possession.getAssetType());
						ord.getSellers().put(this, howMuch);
						PseudoDB.getOrders().add(ord);
					}
				}

			} else {
				synchronized (PseudoDB.rawMaterialsLock) {
					BigDecimal howMuch = new BigDecimal(ThreadLocalRandom.current().nextInt(1,
							Integer.valueOf(possession.getQuantity().intValue() + 1)));
					Order ord = new Order();
					try { // meaning sellers already sells it
						ord = PseudoDB.getOrders().stream().filter(o -> o.getAsset() == possession.getAssetType())
								.collect(Collectors.toList()).get(0);
						ord.getSellers().put(this, howMuch);
					} catch (IndexOutOfBoundsException e) { // doesnt sell it already
						ord.setAsset(possession.getAssetType());
						ord.getSellers().put(this, howMuch);
						PseudoDB.getOrders().add(ord);
					}
				}

			}

		}
	}

	/**
	 * enables an investor to buy some new assets
	 */
	private void tryToBuySomething() {
		switch (ThreadLocalRandom.current().nextInt(1, 4)) {
		case 1:
			buySomeShares();
			break;
		case 2:
			buySomeCurrency();
			break;
		case 3:
			buySomeRawMaterial();
			break;
		case 4:
			buySomeFundShares();
			break;
		}
	}

	/**
	 * responsible for buying fund shares
	 */
	private void buySomeFundShares() {

	}

	/**
	 * handles buying raw materials by the investor
	 */
	private void buySomeRawMaterial() {
		synchronized (PseudoDB.rawMaterialsLock) {
			List<RawMaterial> quotedRawMaterials = PseudoDB.getRawMaterials().stream().filter(c -> c.isQuoted())
					.collect(Collectors.toList());
			if (quotedRawMaterials.size() > 0) {
				RawMaterial material = quotedRawMaterials
						.get(ThreadLocalRandom.current().nextInt(0, quotedRawMaterials.size()));
				try {
					if (PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(material))
							.collect(Collectors.toList()).get(0).getSellers().containsKey(this)) {
						// if you sell it then dont buy it
						PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(material))
								.collect(Collectors.toList()).get(0).getSellers().remove(this);
						return;
					}
				} catch (Exception e) {
					// else
				}

				try { // if you want to buy it then stop here
					if (PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(material))
							.collect(Collectors.toList()).get(0).getBuyers().containsKey(this)) {
						return;
					}

				} catch (Exception e) {

				}

				BigDecimal available = material.getQuantity();

				try {
					BigDecimal temp = BigDecimal.ZERO;
					HashMap<Gambler, BigDecimal> sellers = PseudoDB.getOrders().stream()
							.filter(o -> o.getAsset() == material).collect(Collectors.toList()).get(0).getSellers();
					for (Map.Entry<Gambler, BigDecimal> entry : sellers.entrySet())
						temp = temp.add(entry.getValue());

					available = available.add(temp);
				} catch (IndexOutOfBoundsException e) {
				}
				// co jesli available==0? czyli nie mozna kupic zadnych walut

				BigDecimal max = this.getOperationalBudget().divide(material.getCurrentRate(), 0, RoundingMode.FLOOR);
				BigDecimal possible = available.min(max);
				if (possible.compareTo(BigDecimal.ONE) < 0) {
					return;
				}
				BigDecimal howMany = new BigDecimal(
						ThreadLocalRandom.current().nextInt(1, Integer.valueOf(possible.intValue() + 1)));

				try {
					Order order = PseudoDB.getOrders().stream().filter(o -> o.getAsset() == material)
							.collect(Collectors.toList()).get(0);
					order.getBuyers().put(this, howMany);
					this.setOperationalBudget(
							this.getOperationalBudget().subtract(material.getCurrentRate().multiply(howMany)));
				} catch (IndexOutOfBoundsException e) {// not in orders yet
					Order newOrder = new Order();
					newOrder.setAsset(material);
					newOrder.getBuyers().put(this, howMany);
					PseudoDB.getOrders().add(newOrder);
					this.setOperationalBudget(
							this.getOperationalBudget().subtract(material.getCurrentRate().multiply(howMany)));
				}
			}
		}
	}

	/**
	 * handles buying currencies by the investor
	 */
	private void buySomeCurrency() {
		synchronized (PseudoDB.currenciesLock) {
			List<Currency> quotedCurrencies = PseudoDB.getCurrencies().stream().filter(c -> c.isQuoted())
					.collect(Collectors.toList());
			if (quotedCurrencies.size() > 0) {
				Currency curr = quotedCurrencies.get(ThreadLocalRandom.current().nextInt(0, quotedCurrencies.size()));
				try {
					if (PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(curr))
							.collect(Collectors.toList()).get(0).getSellers().containsKey(this)) {
						// if you sell it then dont buy it
						PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(curr))
								.collect(Collectors.toList()).get(0).getSellers().remove(this);
						return;
					}
				} catch (Exception e) {
					// else
				}

				try { // if you want to buy it then stop here
					if (PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(curr))
							.collect(Collectors.toList()).get(0).getBuyers().containsKey(this)) {
						return;
					}

				} catch (Exception e) {

				}

				BigDecimal available = curr.getQuantity();

				try {
					BigDecimal temp = BigDecimal.ZERO;
					HashMap<Gambler, BigDecimal> sellers = PseudoDB.getOrders().stream()
							.filter(o -> o.getAsset() == curr).collect(Collectors.toList()).get(0).getSellers();
					for (Map.Entry<Gambler, BigDecimal> entry : sellers.entrySet())
						temp = temp.add(entry.getValue());

					available = available.add(temp);
				} catch (IndexOutOfBoundsException e) {
				}
				// co jesli available==0? czyli nie mozna kupic zadnych walut

				BigDecimal max = this.getOperationalBudget().divide(curr.getCurrentRate(), 0, RoundingMode.FLOOR);
				BigDecimal possible = available.min(max);
				if (possible.compareTo(BigDecimal.ONE) < 0) {
					return;
				}
				BigDecimal howMany = new BigDecimal(
						ThreadLocalRandom.current().nextInt(1, Integer.valueOf(possible.intValue() + 1)));

				try {
					synchronized (PseudoDB.sharesLock) {
						synchronized (PseudoDB.rawMaterialsLock) {
							Order order = PseudoDB.getOrders().stream().filter(o -> o.getAsset() == curr)
									.collect(Collectors.toList()).get(0);
							order.getBuyers().put(this, howMany);
							this.setOperationalBudget(
									this.getOperationalBudget().subtract(curr.getCurrentRate().multiply(howMany)));
						}
					}

				} catch (IndexOutOfBoundsException e) {// not in orders yet
					Order newOrder = new Order();
					newOrder.setAsset(curr);
					newOrder.getBuyers().put(this, howMany);
					PseudoDB.getOrders().add(newOrder);
					this.setOperationalBudget(
							this.getOperationalBudget().subtract(curr.getCurrentRate().multiply(howMany)));
				}
			}
		}
	}

	/**
	 * handles buying shares by the investor
	 */
	private void buySomeShares() {
		synchronized (PseudoDB.sharesLock) {
			List<Share> quotedShares = PseudoDB.getShares().stream().filter(s -> s.isQuoted())
					.collect(Collectors.toList());
			if (quotedShares.size() > 0) {
				Share share = quotedShares.get(ThreadLocalRandom.current().nextInt(0, quotedShares.size()));
				try {
					if (PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(share))
							.collect(Collectors.toList()).get(0).getSellers().containsKey(this)) {
						// if you sell it then dont buy it
						PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(share))
								.collect(Collectors.toList()).get(0).getSellers().remove(this);
						return;
					}
				} catch (Exception e) {
					// else
				}

				try { // if you want to buy it then stop here
					if (PseudoDB.getOrders().stream().filter(o -> o.getAsset().equals(share))
							.collect(Collectors.toList()).get(0).getBuyers().containsKey(this)) {
						return;
					}

				} catch (Exception e) {

				}

				BigDecimal available = share.getCompany().getNumOfShares();
				try {
					BigDecimal temp = BigDecimal.ZERO;
					HashMap<Gambler, BigDecimal> sellers = PseudoDB.getOrders().stream()
							.filter(o -> o.getAsset() == share).collect(Collectors.toList()).get(0).getSellers();
					for (Map.Entry<Gambler, BigDecimal> entry : sellers.entrySet())
						temp = temp.add(entry.getValue());

					available = available.add(temp);
				} catch (IndexOutOfBoundsException e) {
				}
				BigDecimal max = this.getOperationalBudget().divide(share.getCurrentRate(), 0, RoundingMode.FLOOR);
				BigDecimal possible = available.min(max);
				if (possible.compareTo(BigDecimal.ONE) < 0) {
					return;
				}
				BigDecimal howMany = new BigDecimal(
						ThreadLocalRandom.current().nextInt(1, Integer.valueOf(possible.intValue() + 1)));

				try {
					Order order = PseudoDB.getOrders().stream().filter(o -> o.getAsset() == share)
							.collect(Collectors.toList()).get(0);
					order.getBuyers().put(this, howMany);
					this.setOperationalBudget(
							this.getOperationalBudget().subtract(share.getCurrentRate().multiply(howMany)));
				} catch (IndexOutOfBoundsException e) {
					Order newOrder = new Order();
					newOrder.setAsset(share);
					newOrder.getBuyers().put(this, howMany);
					PseudoDB.getOrders().add(newOrder);
					this.setOperationalBudget(
							this.getOperationalBudget().subtract(share.getCurrentRate().multiply(howMany)));
				}
			}
		}
	}
}
