package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import stockmarket.utils.PseudoDB;

/**
 * stock exchange index class
 * @author Damian Horna
 *
 */
public class StockExchangeIndex implements Serializable {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 5548738855390839736L;
	/**
	 * name of the index
	 */
	private String name;
	/**
	 * value of the index
	 */
	private BigDecimal value;
	/**
	 * initial value of the index
	 */
	private BigDecimal initial;
	/**
	 * list of the companies
	 */
	private List<Company> companies;
	/**
	 * condition, economic sector of companies
	 */
	private String condition;
	/**
	 * change of the index during session
	 */
	private BigDecimal change;

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	private StockExchange stockExchange;
	private List<RatingRecord> ratingRecords;

	public List<RatingRecord> getRatingRecords() {
		return ratingRecords;
	}

	public void setRatingRecords(List<RatingRecord> ratingRecords) {
		this.ratingRecords = ratingRecords;
	}

	public StockExchangeIndex(String name, BigDecimal value, List<Company> companies, String condition,
			StockExchange stockExchange, List<RatingRecord> ratingRecords) {
		super();
		this.name = name;
		this.value = value;
		this.companies = companies;
		this.condition = condition;
		this.stockExchange = stockExchange;
		this.stockExchange.getIndexes().add(this);
		this.ratingRecords = ratingRecords;
		if (!condition.equals("CUSTOMIZED")) {
			this.companies = stockExchange.getCompanyList().stream()
					.filter(c -> (c.getEconomicSector().equals(condition) && c.isQuoted()))
					.collect(Collectors.toList());
		}

		this.value = new BigDecimal(0);
		this.initial = BigDecimal.ONE;
		this.change = BigDecimal.ZERO;
		BigDecimal val = new BigDecimal(0);

		for (Company c : this.companies) {
			System.out.println("inloop");
			System.out.println("Adding: " + PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getName()))
					.collect(Collectors.toList()).get(0).getCurrentRate().toString());
			val = val.add(PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getName()))
					.collect(Collectors.toList()).get(0).getCurrentRate());
		}
		System.out.println("val: " + val.toString());
		this.value = val;
		this.initial = this.value;
		System.out.println("index initial: " + this.initial.toString());

		RatingRecord ratRec = new RatingRecord(this.value, new Date(), BigDecimal.ZERO);
		if (!(this.value.compareTo(BigDecimal.ZERO) <= 0)) {
			this.ratingRecords.add(ratRec);
		}

	}

	public StockExchangeIndex(String iName, StockExchange stockExchange, List<Company> selectedCompanies,
			String condition) {
		this(iName, BigDecimal.ZERO, selectedCompanies, condition, stockExchange, new ArrayList<>());
	}

	public BigDecimal getInitial() {
		return initial;
	}

	public void setInitial(BigDecimal initial) {
		this.initial = initial;
	}

	public void addCompany() {

	}

	public List<Company> getCompanies() {
		return companies;
	}

	public String getCondition() {
		return condition;
	}

	public String getName() {
		return name;
	}

	public StockExchange getStockExchange() {
		return stockExchange;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void removeCompany() {

	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStockExchange(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "StockExchangeIndex [name=" + name + ", value=" + value + ", companies=" + companies + ", condition="
				+ condition + ", stockExchange=" + stockExchange + "]";
	}
}
