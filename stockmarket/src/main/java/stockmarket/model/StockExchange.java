package stockmarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * stock exchange class
 * @author Damian Horna
 *
 */
public class StockExchange extends Market{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 3584989557405619362L;
	/**
	 * currency of stock exchange
	 */
	private Currency currency;
	/**
	 * companies quoted on the exchange
	 */
	private List<Company> companyList = new ArrayList<Company>();
	/**
	 * list of indexes connected with the exchange
	 */
	private List<StockExchangeIndex> indexes = new ArrayList<>();
	public StockExchange(String name, String country, String city, String address, BigDecimal profitMargin,
			Currency currency, List<Company> companyList) {
		super(name, country, city, address, profitMargin);
		this.currency = currency;
		this.companyList = companyList;
	}
	public List<StockExchangeIndex> getIndexes() {
		return indexes;
	}
	public void setIndexes(List<StockExchangeIndex> indexes) {
		this.indexes = indexes;
	}
	public StockExchange(String name, String country, String city, String address, BigDecimal profitMargin,
			Currency currency) {
		this(name,country,city,address,profitMargin,currency,new ArrayList<>());
	}
	public List<Company> getCompanyList() {
		return companyList;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		return "StockExchange [currency=" + currency + ", companyList=" + companyList +", name="+super.getName() +"]";
	}

}
