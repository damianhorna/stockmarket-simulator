package stockmarket.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import stockmarket.model.Company;
import stockmarket.model.Currency;
import stockmarket.model.ExchangeMarket;
import stockmarket.model.InvestmentFund;
import stockmarket.model.Investor;
import stockmarket.model.RawMaterial;
import stockmarket.model.RawMaterialsMarket;
import stockmarket.model.StockExchange;
import stockmarket.model.StockExchangeIndex;
import stockmarket.model.Unit;

/**
 * generator of random data
 * @author Damian Horna
 *
 */
public class RandomDataGenerator {

	/**
	 * list of random names
	 */
	private static List<String> names = new ArrayList<>();
	/**
	 * list of random surnames
	 */
	private static List<String> surnames = new ArrayList<>();
	/**
	 * list of all capitals
	 */
	private static List<String> capitals = new ArrayList<>();
	/**
	 * list of all countries
	 */
	private static List<String> countries = new ArrayList<>();
	/**
	 * list of all economic sectors in the simulation
	 */
	private static List<String> sectors = new ArrayList<>(new ArrayList<>(
			Arrays.asList("IT", "BANKS", "TELECOMMUNICATION", "CONSTRUCTION", "GROCERY", "MEDIA", "ENERGY")));
	/**
	 * list of investment funds in the simulation
	 */
	private static List<String> iFunds = new ArrayList<>();
	/**
	 * list of stock exchanges
	 */
	private static List<String> stockExchanges = new ArrayList<>();
	/**
	 * list of companies available in the simulation
	 */
	private static List<String> companies = new ArrayList<>();
	/**
	 * list of available currencies
	 */
	private static List<String> currenciesAvailable = new ArrayList<>();
	/**
	 * list of random and unique pesels
	 */
	private static List<String> pesels = new ArrayList<>();
	/**
	 * list of countries that haven't been used yet
	 */
	private static List<String> countriesForCurrencies = new ArrayList<>();
	/**
	 * list of available raw materials
	 */
	private static List<String> rawMaterialsAvailable = new ArrayList<>();

	public List<String> getCountriesForCurrencies() {
		return countriesForCurrencies;
	}

	public List<String> getCurrenciesAvailable() {
		return currenciesAvailable;
	}

	public  void setCurrenciesAvailable(List<String> currenciesAvailable) {
		RandomDataGenerator.currenciesAvailable = currenciesAvailable;
	}

	public List<String> getRawMaterialsAvailable() {
		return rawMaterialsAvailable;
	}

	public  void setRawMaterialsAvailable(List<String> rawMaterialsAvailable) {
		RandomDataGenerator.rawMaterialsAvailable = rawMaterialsAvailable;
	}

	public void setCountriesForCurrencies(List<String> countriesForCurrencies) {
		RandomDataGenerator.countriesForCurrencies = countriesForCurrencies;
	}

	ClassLoader classLoader = getClass().getClassLoader();

	/**
	 * initializes the data in the list from files
	 * @throws IOException
	 */
	public void initialize() throws IOException {

		readData(this.getClass().getResourceAsStream("/random/names.txt"), names);
		readData(this.getClass().getResourceAsStream("/random/surnames.txt"), surnames);
		readData(this.getClass().getResourceAsStream("/random/capitals.txt"), capitals);
		readData(this.getClass().getResourceAsStream("/random/funds.txt"), iFunds);
		readData(this.getClass().getResourceAsStream("/random/stockExchanges.txt"), stockExchanges);
		readData(this.getClass().getResourceAsStream("/random/companies.txt"), companies);
		readData(this.getClass().getResourceAsStream("/random/raw.txt"), rawMaterialsAvailable);
		readData(this.getClass().getResourceAsStream("/random/currencies.txt"), currenciesAvailable);
		readData(this.getClass().getResourceAsStream("/random/pes.txt"), pesels);

		String[] locales = Locale.getISOCountries();
		Locale locale = new Locale("en");
		for (String countryCode : locales) {
			Locale obj = new Locale("", countryCode);
			countries.add(obj.getDisplayCountry(locale));
			countriesForCurrencies.add(obj.getDisplayCountry(locale));
		}
		
	}

	
	/**
	 * @param list list of items
	 * @return returns random item from the list
	 */
	private String getRandom(List<String> list) {
		return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
	}

	/**
	 * @param nextInt how many to remove
	 * @param given given list
	 * @return returns a few random items
	 */
	private List<String> getRandomElements(int nextInt, List<String> given) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < nextInt; i++) {
			int idx = ThreadLocalRandom.current().nextInt(0, given.size());
			list.add(given.get(idx));
			given.remove(idx);
		}
		return list;
	}

	/**
	 * reads data from file
	 * @param path path to the file
	 * @param list list to write data to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readData(InputStream path, List<String> list) throws FileNotFoundException, IOException {
		InputStreamReader isr = new InputStreamReader(path);
		BufferedReader abc = new BufferedReader(isr);
		String line;
		while ((line = abc.readLine()) != null) {
			list.add(line);
		}
		abc.close();
	}

	/**
	 * creates new investors
	 * @param quan how many investors to create
	 */
	public void newInvestors(int quan) {
		for (int i = 0; i < quan; i++) {
			Integer j = ThreadLocalRandom.current().nextInt(1000, 10000 + 1);
			Investor investor = new Investor(getRandom(names), getRandom(surnames), getRandom(pesels), j.toString());
			PseudoDB.addNewInwestor(investor);
			Thread t = new Thread(investor);
			t.start();
		}
		PseudoDB.showAllInvestors();
	}
	/**
	 * creates new investment funds
	 * @param quan how many investment funds to create
	 */
	public void newInvestmentFunds(int quan) {
		for (int i = 0; i < quan; i++) {
			Integer j = ThreadLocalRandom.current().nextInt(5000, 50000 + 1);
			InvestmentFund investmentFund = new InvestmentFund(getRandom(iFunds), getRandom(names), getRandom(surnames),
					j.toString());
			PseudoDB.addNewInwestmentFund(investmentFund);
		}
		PseudoDB.showAllInvestmentFunds();
	}
	/**
	 * creates new companies
	 * @param quan how many companies to create
	 */
	public void newCompanies(int quan) {
		if(companies.size()<quan) {
			throw new IllegalArgumentException("too many");
		}
		for (int i = 0; i < quan; i++) {
			String name = getRandom(companies);
			Company company = new Company(name, getRandom(sectors));
			companies.remove(name);
			PseudoDB.addNewCompany(company);
			Thread t = new Thread(company);
			t.start();
		}
		PseudoDB.shawAllCompanies();
	}
	/**
	 * creates new currencies
	 * @param quan how many currencies to create
	 */
	public void newCurrencies(int quan) {
		if (countriesForCurrencies.size() == 0 || currenciesAvailable.size() == 0 || quan > currenciesAvailable.size()
				|| quan > countriesForCurrencies.size())
			throw new IllegalArgumentException("You cannot add more currencies!");
		else {

			for (int i = 0; i < quan; i++) {
				List<String> selected = getRandomElements(ThreadLocalRandom.current().nextInt(1, 3 + 1),
						countriesForCurrencies);
				String crncy = getRandom(currenciesAvailable);
				Currency currency = new Currency(crncy, selected);
				currenciesAvailable.remove(crncy);
				countriesForCurrencies.removeAll(selected);
				PseudoDB.addNewCurrency(currency);
			}
			PseudoDB.showAllCurrencies();
		}
	}
	/**
	 * creates new raw materials
	 * @param quan how many raw materials to create
	 */
	public void newRawMaterials(int quan) {
		if(PseudoDB.getCurrencies().size()==0 || rawMaterialsAvailable.size() == 0) throw new IllegalArgumentException("you can not add more raw materials(do it manually)");
		for (int i = 0; i < quan; i++) {
			Currency c = PseudoDB.getCurrencies()
					.get(ThreadLocalRandom.current().nextInt(0, PseudoDB.getCurrencies().size()));
			String rmName = getRandom(rawMaterialsAvailable);
			RawMaterial rawMaterial = new RawMaterial(rmName,
					Unit.valueOf(getRandom(Arrays.asList(Unit.getNames(Unit.class)))), c);
			rawMaterialsAvailable.remove(rmName);
			PseudoDB.addNewRawMaterial(rawMaterial);
		}
		PseudoDB.showAllRawMaterials();
	}
	/**
	 * creates new raw materials markets
	 * @param quan how many raw materials markets to create
	 */
	public void newRawMaterialsMarket(int quan) {

		for (int i = 0; i < quan; i++) {
			String city = getRandom(capitals);
			RawMaterialsMarket eMarket = new RawMaterialsMarket(city + " Raw Materials Market", getRandom(countries),
					city, city + "street 13", new BigDecimal(ThreadLocalRandom.current().nextDouble(0,1)).setScale(2, BigDecimal.ROUND_HALF_UP));
			PseudoDB.addNewRawMaterialsMarket(eMarket);
		}
		PseudoDB.showAllRawMaterialsMarkets();
	}
	/**
	 * creates new stock exchanges
	 * @param quan how many stock exchanges to create
	 */
	public void newStockExchange(int quan) {
		List<String> possibleCountries = new ArrayList<>(countries);
		System.out.println("all: "+ countries);
		possibleCountries.removeAll(countriesForCurrencies);
		System.out.println("countries for currencies: "+ countriesForCurrencies);
		System.out.println("Possible countries at new stock exchanges method: "+possibleCountries);

		for (int i = 0; i < quan; i++) {
			if(PseudoDB.getCurrencies().size()==0) {
				throw new IllegalArgumentException("no currencies available");
			}
			String city = getRandom(capitals);
			String country = getRandom(possibleCountries);
			
			StockExchange eMarket = new StockExchange(city+ " Stock Exchange", country, city, city +"street 7", new BigDecimal(ThreadLocalRandom.current().nextDouble(0,1)).setScale(2, BigDecimal.ROUND_HALF_UP),
					getCurrencyOfCountry(country));
			PseudoDB.addNewStockExchange(eMarket);
		}
		PseudoDB.showAllStockExchanges();
	}

	
	/**
	 * @param country country to investigate
	 * @return currency of a given country
	 */
	private Currency getCurrencyOfCountry(String country) {
		List<Currency> availableCurrencies = PseudoDB.getCurrencies();
		for(Currency c: availableCurrencies) {
			if (c.getCountryList().contains(country)) return c;
		}
		return null;
	}

	/**
	 * creates new indexes
	 * @param quan how many indexes to create
	 */
	public void newStockExchangeIndex(int quan) {
		if(PseudoDB.getStockExchanges().size()==0) {
			throw new IllegalArgumentException("too few stock exchanges");
		}else {
			for (int i = 0; i < quan; i++) {
				StockExchange se = PseudoDB.getStockExchanges().get(ThreadLocalRandom.current().nextInt(0,PseudoDB.getStockExchanges().size()));
				List<Company> selectedCompanies = new ArrayList<>();
				String city = getRandom(capitals);
				String cond = getRandomButNotZero(PseudoDB.getConditions());
				StockExchangeIndex sei = new StockExchangeIndex(city+"-"+cond,se,
						selectedCompanies, cond);
				PseudoDB.addNewStockExchangeIndex(sei);
			}
			PseudoDB.showAllStockExchangeIndexes();
		}
	}

	/**
	 * @param conditions list of all conditions
	 * @return returns random number of conditions
	 */
	private String getRandomButNotZero(List<String> conditions) {
		return conditions.get(ThreadLocalRandom.current().nextInt(1, conditions.size()));
	}

	/**
	 * creates new exchange markets
	 * @param quan how many exchange markets to create
	 */
	public void newExchangeMarkets(int quan) {
		for (int i = 0; i < quan; i++) {
			String city = getRandom(capitals);
			ExchangeMarket eMarket = new ExchangeMarket(city + " Exchange Market", getRandom(countries), city,
					city + "street 29", new BigDecimal(ThreadLocalRandom.current().nextDouble(0,1)).setScale(2, BigDecimal.ROUND_HALF_UP));
			PseudoDB.addNewExchangeMarket(eMarket);
		}
		PseudoDB.showAllExchangeMarkets();
	}
}
