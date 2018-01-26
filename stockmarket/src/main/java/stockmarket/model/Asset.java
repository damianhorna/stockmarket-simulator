package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * asset class
 * @author Damian Horna
 *
 */
/**
 * @author damia
 *
 */
public abstract class Asset implements Serializable {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -2755209305059257314L;
	
	/**
	 * name of the asset
	 */
	private String name;

	/**
	 * first appraisal date of the asset
	 */
	private Date firstAppraisalDate;
	
	/**
	 * the highest value of the asset
	 */
	private BigDecimal allTimeHigh;
	
	/**
	 * the lowest value of the asset
	 */
	private BigDecimal allTimeLow;

	/**
	 * initial value of the asset
	 */
	private BigDecimal initialRate;

	/**
	 * minimal value of the asset during session
	 */
	private BigDecimal minimalRate;

	/**
	 * current value of the asset
	 */
	private BigDecimal currentRate;

	/**
	 * maximal value of the asset during session
	 */
	private BigDecimal maximalRate;

	/**
	 * change of the asset in current session
	 */
	private BigDecimal change;
	
	/**
	 * percentage change
	 */
	private BigDecimal changePct;

	/**
	 * market on which asset is quoted
	 */
	private Market market;

	/**
	 * list of rating records of the asset
	 */
	private List<RatingRecord> ratingRecords = new ArrayList<>();

	/**
	 * if an asset is quoted then this variale is true
	 */
	private boolean isQuoted;


	public Asset(String name) {
		
		this(name,new Date(), BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,null,new ArrayList<>(),false,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
	}

	public Asset(String name, Date firstAppraisalDate, BigDecimal initialRate, BigDecimal minimalRate,
			BigDecimal currentRate, BigDecimal maximalRate, BigDecimal change, Market market,
			List<RatingRecord> theRatingRecords, boolean isQuoted, BigDecimal allTimeH, BigDecimal allTimeL, BigDecimal chPtc) {
		super();
		this.name = name;
		this.firstAppraisalDate = firstAppraisalDate;
		this.initialRate = initialRate;
		this.minimalRate = minimalRate;
		this.currentRate = currentRate;
		this.maximalRate = maximalRate;
		this.change = change;
		this.market = market;
		this.ratingRecords = theRatingRecords;
		this.isQuoted = isQuoted;
		this.allTimeHigh = allTimeH;
		this.allTimeLow = allTimeL;
		this.changePct=chPtc;
	}

	/**
	 * abstract buy method
	 */
	public abstract void buy();

	public BigDecimal getAllTimeHigh() {
		return allTimeHigh;
	}

	public BigDecimal getAllTimeLow() {
		return allTimeLow;
	}

	public BigDecimal getChange() {
		return change;
	}

	public BigDecimal getChangePct() {
		return changePct;
	}

	public BigDecimal getCurrentRate() {
		return currentRate;
	}

	public Date getFirstAppraisalDate() {
		return firstAppraisalDate;
	}

	public BigDecimal getInitialRate() {
		return initialRate;
	}

	public Market getMarket() {
		return market;
	}

	public BigDecimal getMaximalRate() {
		return maximalRate;
	}

	public BigDecimal getMinimalRate() {
		return minimalRate;
	}

	public String getName() {
		return name;
	}

	public List<RatingRecord> getRatingRecords() {
		return ratingRecords;
	}
	
	public abstract void initialize();
	public boolean isQuoted() {
		return isQuoted;
	}
	public abstract void sell();
	public void setAllTimeHigh(BigDecimal allTimeHigh) {
		this.allTimeHigh = allTimeHigh;
	}
	public void setAllTimeLow(BigDecimal allTimeLow) {
		this.allTimeLow = allTimeLow;
	}
	public void setChange(BigDecimal change) {
		this.change = change;
	}
	public void setChangePct(BigDecimal changePct) {
		this.changePct = changePct;
	}
	public void setCurrentRate(BigDecimal currentRate) {
		this.currentRate = currentRate;
	}

	public void setFirstAppraisalDate(Date firstAppraisalDate) {
		this.firstAppraisalDate = firstAppraisalDate;
	}

	public void setInitialRate(BigDecimal initialRate) {
		this.initialRate = initialRate;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public void setMaximalRate(BigDecimal maximalRate) {
		this.maximalRate = maximalRate;
	}

	public void setMinimalRate(BigDecimal minimalRate) {
		this.minimalRate = minimalRate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuoted(boolean isQuoted) {
		this.isQuoted = isQuoted;
	}

	public void setRatingRecords(List<RatingRecord> ratingRecords) {
		this.ratingRecords = ratingRecords;
	}
	
	@Override
	public String toString() {
		return "Asset [name=" + name + ", firstAppraisalDate=" + firstAppraisalDate + ", initialRate=" + initialRate
				+ ", minimalRate=" + minimalRate + ", currentRate=" + currentRate + ", maximalRate=" + maximalRate
				+ ", change=" + change + ", market=" + market + ", ratingRecords=" + ratingRecords + ", isQuoted="
				+ isQuoted + "]";
	}

}
