package stockmarket.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import stockmarket.model.Company;
import stockmarket.model.Currency;
import stockmarket.model.ExchangeMarket;
import stockmarket.model.InvestmentFund;
import stockmarket.model.Investor;
import stockmarket.model.Order;
import stockmarket.model.RawMaterial;
import stockmarket.model.RawMaterialsMarket;
import stockmarket.model.Share;
import stockmarket.model.StockExchange;
import stockmarket.model.StockExchangeIndex;

/**
 * pseudo dB class that holds all the objects available in the simulation
 * @author Damian Horna
 *
 */
public class PseudoDB{
	/**
	 * lock on shares, so that objects of many types wont perform any operation on mutual items in pseudo data base
	 */
	public static Object sharesLock = new Object();
	/**
	 * lock on currencies, so that objects of many types wont perform any operation on mutual items in pseudo data base
	 */
	public static Object currenciesLock = new Object();
	/**
	 * lock on raw materials, so that objects of many types wont perform any operation on mutual items in pseudo data base
	 */
	public static Object rawMaterialsLock = new Object();
	/**
	 * list of all investors available in the simulation
	 */
	private static List<Investor> investors = new ArrayList<>();
	/**
	 * list of all currencies available in the simulation
	 */
	private static List<Currency> currencies = new ArrayList<>();
	/**
	 * list of all companies available in the simulation
	 */
	private static List<Company> companies = new ArrayList<>();
	/**
	 * list of all exchange markets available in the simulation
	 */
	private static List<ExchangeMarket> exchangeMarkets = new ArrayList<>();
	/**
	 * list of all investment funds available in the simulation
	 */
	private static List<InvestmentFund> investmentFunds = new ArrayList<>();
	/**
	 * list of all raw materials markets available in the simulation
	 */
	private static List<RawMaterialsMarket> rawMaterialsMarkets = new ArrayList<>();
	/**
	 * list of all raw materials available in the simulation
	 */
	private static List<RawMaterial> rawMaterials = new ArrayList<>();
	/**
	 * list of all stock exchanges available in the simulation
	 */
	private static List<StockExchange> stockExchanges = new ArrayList<>();
	/**
	 * list of all indexes available in the simulation
	 */
	private static List<StockExchangeIndex> stockExchangeIndexes = new ArrayList<>();
	/**
	 * list of all countries available in the simulation
	 */
	private static List<String> countries = new ArrayList<>();
	/**
	 * list of all economic sectors available in the simulation
	 */
	private static List<String> sectors = new ArrayList<>(new ArrayList<>(Arrays.asList("IT", "BANKS","TELECOMMUNICATION","CONSTRUCTION","GROCERY","MEDIA","ENERGY")));
	/**
	 * list of all conditions available in the simulation
	 */
	private static List<String> conditions = new ArrayList<>(Arrays.asList("None","IT", "BANKS", "TELECOMMUNICATION", "CONSTRUCTION", "GROCERY", "MEDIA", "ENERGY"));
	/**
	 * auto adding boolean, true if enabled
	 */
	private static boolean autoAdding = false;
	/**
	 * list of all saves
	 */
	private static List<String> saves = new ArrayList<>();
	/**
	 * list of all shares
	 */
	private static List<Share> shares = new ArrayList<>();
	/**
	 * list of orders
	 */
	private static List<Order> orders = new ArrayList<>();


	public synchronized static List<Order> getOrders() {
		return orders;
	}

	public synchronized static void setOrders(List<Order> orders) {
		PseudoDB.orders = orders;
	}

	public static List<Share> getShares() {
		return shares;
	}

	public static void setShares(List<Share> shares) {
		PseudoDB.shares = shares;
	}

	public static boolean isAutoAdding() {
		return autoAdding;
	}

	public static List<String> getSaves() {
		return saves;
	}

	public static void setSaves(List<String> saves) {
		PseudoDB.saves = saves;
	}

	public static void setAutoAdding(boolean autoAdding) {
		PseudoDB.autoAdding = autoAdding;
	}

	public static List<String> getConditions() {
		return conditions;
	}

	public static void setConditions(List<String> conditions) {
		PseudoDB.conditions = conditions;
	}

	public static List<String> getCountries(Locale locale) {
		String[] locales = Locale.getISOCountries();

		for (String countryCode : locales) {
			Locale obj = new Locale("", countryCode);
			countries.add(obj.getDisplayCountry(locale));
		}
		return countries;
	}
	
	public static List<String> getCountries() {
		return countries;
	}

	public static void setSectors(List<String> sectors) {
		PseudoDB.sectors = sectors;
	}

	public static List<String> getSectors(){
		return sectors;
	}

	public static void setCountries(List<String> countries) {
		PseudoDB.countries = countries;
	}

	public static List<Investor> getInvestors() {
		return investors;
	}

	public static void setInvestors(List<Investor> investors) {
		PseudoDB.investors = investors;
	}

	public static List<Currency> getCurrencies() {
		return currencies;
	}

	public static void setCurrencies(List<Currency> currencies) {
		PseudoDB.currencies = currencies;
	}

	public static List<Company> getCompanies() {
		return companies;
	}

	public static void setCompanies(List<Company> companies) {
		PseudoDB.companies = companies;
	}

	public static List<ExchangeMarket> getExchangeMarkets() {
		return exchangeMarkets;
	}

	public static void setExchangeMarkets(List<ExchangeMarket> exchangeMarkets) {
		PseudoDB.exchangeMarkets = exchangeMarkets;
	}

	public static List<InvestmentFund> getInvestmentFunds() {
		return investmentFunds;
	}

	public static void setInvestmentFunds(List<InvestmentFund> investmentFunds) {
		PseudoDB.investmentFunds = investmentFunds;
	}

	public static List<RawMaterialsMarket> getRawMaterialsMarkets() {
		return rawMaterialsMarkets;
	}

	public static void setRawMaterialsMarkets(List<RawMaterialsMarket> rawMaterialsMarkets) {
		PseudoDB.rawMaterialsMarkets = rawMaterialsMarkets;
	}

	public static List<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}

	public static void setRawMaterials(List<RawMaterial> rawMaterials) {
		PseudoDB.rawMaterials = rawMaterials;
	}

	public static List<StockExchange> getStockExchanges() {
		return stockExchanges;
	}

	public static void setStockExchanges(List<StockExchange> stockExchanges) {
		PseudoDB.stockExchanges = stockExchanges;
	}

	public static List<StockExchangeIndex> getStockExchangeIndexes() {
		return stockExchangeIndexes;
	}

	public static void setStockExchangeIndexes(List<StockExchangeIndex> stockExchangeIndexes) {
		PseudoDB.stockExchangeIndexes = stockExchangeIndexes;
	}

	public static void addNewInwestor(Investor investor) {

		investors.add(investor);
	}

	public static void showAllInvestors() {
		investors.forEach(i -> System.out.println(i));
	}

	public static void addNewCompany(Company company) {
		companies.add(company);
	}

	public static void shawAllCompanies() {
		companies.forEach(System.out::println);
	}

	public static void addNewCurrency(Currency currency) {
		currencies.add(currency);
	}

	public static void showAllCurrencies() {
		currencies.forEach(System.out::println);
	}

	public static void showAllExchangeMarkets() {
		exchangeMarkets.forEach(System.out::println);
	}

	public static void addNewExchangeMarket(ExchangeMarket market) {
		exchangeMarkets.add(market);
	}

	public static void showAllInvestmentFunds() {
		investmentFunds.forEach(System.out::println);
	}

	public static void addNewInwestmentFund(InvestmentFund investmentFund) {
		investmentFunds.add(investmentFund);
	}

	public static void addNewRawMaterialsMarket(RawMaterialsMarket eMarket) {
		rawMaterialsMarkets.add(eMarket);
	}

	public static void showAllRawMaterialsMarkets() {
		rawMaterialsMarkets.forEach(System.out::println);
	}

	public static Currency getCurrencyByName(String rCurrency) {
		return currencies.stream().filter(c -> c.getName().equals(rCurrency)).collect(Collectors.toList()).get(0);
	}

	public static void showAllRawMaterials() {
		rawMaterials.forEach(System.out::println);
	}

	public static void addNewRawMaterial(RawMaterial rawMaterial) {
		rawMaterials.add(rawMaterial);
	}

	public static void addNewStockExchange(StockExchange eMarket) {
		stockExchanges.add(eMarket);
	}

	public static void showAllStockExchanges() {
		stockExchanges.forEach(System.out::println);
	}

	public static StockExchange getStockExchangeByName(String name) {
		return stockExchanges.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList()).get(0);
	}

	public static List<Company> getCompaniesByNames(List<String> selected) {
		return companies.stream().filter(c -> selected.contains(c.getName())).collect(Collectors.toList());
	}

	public static void addNewStockExchangeIndex(StockExchangeIndex sei) {
		stockExchangeIndexes.add(sei);
	}

	public static void showAllStockExchangeIndexes() {
		stockExchangeIndexes.forEach(System.out::println);
	}

	public static ExchangeMarket getExchangeMarketByName(String name) {
		return exchangeMarkets.stream().filter(e->e.getName().equals(name)).collect(Collectors.toList()).get(0);
	}

	public static RawMaterialsMarket getRawMaterialsMarketByName(String name) {
		return rawMaterialsMarkets.stream().filter(e->e.getName().equals(name)).collect(Collectors.toList()).get(0);
	}

	public static RawMaterial getRawMaterialByName(String name) {
		return rawMaterials.stream().filter(e->e.getName().equals(name)).collect(Collectors.toList()).get(0);
	}

}
