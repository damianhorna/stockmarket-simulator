package stockmarket.simulation;

import java.io.Serializable;
import java.util.List;

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
 * non static class that holds all the data needed to be saved in order to reconstruct the simulation when loading
 * @author Damian Horna
 *
 */
public class SimulationState implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -3437832433263098191L;
	/**
	 * list of all investors
	 */
	private List<Investor> investors;
	/**
	 * list of all currencies
	 */
	private List<Currency> currencies;
	/**
	 * list of all companies
	 */
	private List<Company> companies;
	/**
	 * list of all exchange markets
	 */
	private List<ExchangeMarket> exchangeMarkets;
	/**
	 * list of all investment funds
	 */
	private List<InvestmentFund> investmentFunds;
	/**
	 * list of all raw materials markets
	 */
	private List<RawMaterialsMarket> rawMaterialsMarkets;
	/**
	 * list of all raw materials
	 */
	private List<RawMaterial> rawMaterials;
	/**
	 * list of all stock exchanges
	 */
	private List<StockExchange> stockExchanges;
	/**
	 * list of all stock exchange indexes
	 */
	private List<StockExchangeIndex> stockExchangeIndexes;
	/**
	 * list of all countries
	 */
	private List<String> countries;
	/**
	 * auto adding enabled or not
	 */
	private boolean autoAdding;
	/**
	 * list of all shares in the simulation
	 */
	private List<Share> shares;
	/**
	 * list of all orders
	 */
	private List<Order> orders;
	
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public List<Share> getShares() {
		return shares;
	}
	public void setShares(List<Share> shares) {
		this.shares = shares;
	}
	public boolean isAutoAdding() {
		return autoAdding;
	}
	public void setAutoAdding(boolean autoAdding) {
		this.autoAdding = autoAdding;
	}
	public List<Investor> getInvestors() {
		return investors;
	}
	public void setInvestors(List<Investor> investors) {
		this.investors = investors;
	}
	public SimulationState(List<Investor> investors, List<Currency> currencies, List<Company> companies,
			List<ExchangeMarket> exchangeMarkets, List<InvestmentFund> investmentFunds,
			List<RawMaterialsMarket> rawMaterialsMarkets, List<RawMaterial> rawMaterials,
			List<StockExchange> stockExchanges, List<StockExchangeIndex> stockExchangeIndexes, List<String> countries, boolean autoAdding, List<Share> shares, List<Order> orders) {
		super();
		this.investors = investors;
		this.currencies = currencies;
		this.companies = companies;
		this.exchangeMarkets = exchangeMarkets;
		this.investmentFunds = investmentFunds;
		this.rawMaterialsMarkets = rawMaterialsMarkets;
		this.rawMaterials = rawMaterials;
		this.stockExchanges = stockExchanges;
		this.stockExchangeIndexes = stockExchangeIndexes;
		this.countries = countries;
		this.autoAdding=autoAdding;
		this.shares=shares;
		this.orders=orders;
	}
	public List<Currency> getCurrencies() {
		return currencies;
	}
	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}
	public List<Company> getCompanies() {
		return companies;
	}
	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
	public List<ExchangeMarket> getExchangeMarkets() {
		return exchangeMarkets;
	}
	public void setExchangeMarkets(List<ExchangeMarket> exchangeMarkets) {
		this.exchangeMarkets = exchangeMarkets;
	}
	public List<InvestmentFund> getInvestmentFunds() {
		return investmentFunds;
	}
	public void setInvestmentFunds(List<InvestmentFund> investmentFunds) {
		this.investmentFunds = investmentFunds;
	}
	public List<RawMaterialsMarket> getRawMaterialsMarkets() {
		return rawMaterialsMarkets;
	}
	public void setRawMaterialsMarkets(List<RawMaterialsMarket> rawMaterialsMarkets) {
		this.rawMaterialsMarkets = rawMaterialsMarkets;
	}
	public List<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}
	public void setRawMaterials(List<RawMaterial> rawMaterials) {
		this.rawMaterials = rawMaterials;
	}
	public List<StockExchange> getStockExchanges() {
		return stockExchanges;
	}
	public void setStockExchanges(List<StockExchange> stockExchanges) {
		this.stockExchanges = stockExchanges;
	}
	public List<StockExchangeIndex> getStockExchangeIndexes() {
		return stockExchangeIndexes;
	}
	public void setStockExchangeIndexes(List<StockExchangeIndex> stockExchangeIndexes) {
		this.stockExchangeIndexes = stockExchangeIndexes;
	}
	public List<String> getCountries() {
		return countries;
	}
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
}
