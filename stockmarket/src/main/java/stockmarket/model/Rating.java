package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * rating class
 * @author Damian Horna
 *
 */
public class Rating implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 4495672098400355398L;
	/**
	 * first appraisal date of the asset
	 */
	private Date firstAppraisalDate;
	/**
	 * initial rate of the asset
	 */
	private BigDecimal initialRate;
	/**
	 * minimal rate during session
	 */
	private BigDecimal minimalRate;
	/**
	 * current rate of the asset
	 */
	private BigDecimal currentRate;
	/**
	 * maximal rate during session
	 */
	private BigDecimal maximalRate;
	/**
	 * change of the asset
	 */
	private BigDecimal change;
	public BigDecimal getChange() {
		return change;
	}
	public void setChange(BigDecimal change) {
		this.change = change;
	}
	public Market getMarket() {
		return market;
	}
	public void setMarket(Market market) {
		this.market = market;
	}
	private Market market;
	private List<RatingRecord> ratingRecords = new ArrayList<>();
	public Rating(Date firstAppraisalDate, BigDecimal initialRate, BigDecimal minimalRate, BigDecimal currentRate,
			BigDecimal maximalRate, Market stockExchange, List<RatingRecord> ratingRecords, BigDecimal change) {
		super();
		this.firstAppraisalDate = firstAppraisalDate;
		this.initialRate = initialRate;
		this.minimalRate = minimalRate;
		this.currentRate = currentRate;
		this.maximalRate = maximalRate;
		this.market = stockExchange;
		this.ratingRecords = ratingRecords;
		this.change=change;
	}
	public Rating() {
		
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
	public BigDecimal getMaximalRate() {
		return maximalRate;
	}
	public BigDecimal getMinimalRate() {
		return minimalRate;
	}
	public List<RatingRecord> getRatingRecords() {
		return ratingRecords;
	}
	public Market getStockExchange() {
		return market;
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
	public void setMaximalRate(BigDecimal maximalRate) {
		this.maximalRate = maximalRate;
	}
	public void setMinimalRate(BigDecimal minimalRate) {
		this.minimalRate = minimalRate;
	}
	public void setRatingRecords(List<RatingRecord> ratingRecords) {
		this.ratingRecords = ratingRecords;
	}
	public void setStockExchange(StockExchange stockExchange) {
		this.market = stockExchange;
	}
	@Override
	public String toString() {
		return "Rating [firstAppraisalDate=" + firstAppraisalDate + ", initialRate=" + initialRate + ", minimalRate="
				+ minimalRate + ", currentRate=" + currentRate + ", maximalRate=" + maximalRate + ", stockExchange="
				+ market + ", ratingRecords=" + ratingRecords + "]";
	}
}
