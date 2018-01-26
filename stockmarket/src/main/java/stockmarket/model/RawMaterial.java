package stockmarket.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * raw material class
 * @author Damian Horna
 *
 */
public class RawMaterial extends Asset {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -6607367417484867462L;
	/**
	 * unit of measurement
	 */
	private Unit unit;
	/**
	 * currency in which is being sold
	 */
	private Currency currency;
	/**
	 * list of owners
	 */
	private List<Gambler> owners = new ArrayList<>();
	/**
	 * quantity of a raw material
	 */
	private BigDecimal quantity;

	public List<Gambler> getOwners() {
		return owners;
	}

	public void setOwners(List<Gambler> owners) {
		this.owners = owners;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public RawMaterial(String name, Unit unit, Currency currency) {
		super(name);
		this.unit = unit;
		this.currency = currency;
		this.quantity = new BigDecimal(ThreadLocalRandom.current().nextInt(100000, 500000));
	}

	public Currency getCurrency() {
		return currency;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "RawMaterial [name=" + super.getName() + ", unit=" + unit + ", currency=" + currency + "]";
	}

	@Override
	public void buy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sell() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		BigDecimal initRate = new BigDecimal(ThreadLocalRandom.current().nextDouble(0.2, 100));
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
