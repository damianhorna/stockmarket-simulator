package stockmarket.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * currency class
 * @author Damian Horna
 *
 */
public class Currency extends Asset {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -3192340182673347345L;
	/**
	 * list of countries the currency is available in
	 */
	private List<String> countryList = new ArrayList<>();
	/**
	 * list of owners of currency
	 */
	private List<Gambler> owners = new ArrayList<>();
	/**
	 * quantity of a currency
	 */
	private BigDecimal quantity;


	public Currency(String name, List<String> countryList) {
		super(name);
		this.countryList = countryList;
		this.quantity = new BigDecimal(ThreadLocalRandom.current().nextInt(100000, 500000));
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public List<Gambler> getOwners() {
		return owners;
	}

	public void setOwners(List<Gambler> owners) {
		this.owners = owners;
	}

	public List<String> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<String> countryList) {
		this.countryList = countryList;
	}

	@Override
	public String toString() {
		return "Currency [name=" + super.getName() + ", countryList=" + countryList + "]";
	}

	@Override
	public void buy() {
		//handled already within controller

	}

	@Override
	public void sell() {
		//handled already within controller

	}

	@Override
	public void initialize() {
		BigDecimal initRate = new BigDecimal(ThreadLocalRandom.current().nextDouble(0.5, 3));
		initRate = initRate.setScale(2, RoundingMode.CEILING);
		RatingRecord ratRec = new RatingRecord(initRate, new Date(), new BigDecimal(0));
		this.getRatingRecords().add(ratRec);
		this.setInitialRate(initRate);

		this.setAllTimeHigh(initRate);
		this.setAllTimeLow(initRate);
		this.setInitialRate(initRate);
		this.setMinimalRate(initRate);
		this.setCurrentRate(initRate);
		this.setMaximalRate(initRate);
		this.setChange(BigDecimal.ZERO);
		this.setChangePct(BigDecimal.ZERO);
		this.setQuoted(true);
	}
}
