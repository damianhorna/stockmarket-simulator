package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * rating record class
 * @author Damian Horna
 *
 */
public class RatingRecord implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -326777597008928610L;
	/**
	 * rate in the specific time
	 */
	private BigDecimal rate;
	/**
	 * point in time
	 */
	private Date time;
	/**
	 * volume
	 */
	private BigDecimal volume;
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public RatingRecord(BigDecimal rate, Date time,BigDecimal volume) {
		super();
		this.rate = rate;
		this.time = time;
		this.volume = volume;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public Date getTime() {
		return time;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "RatingRecord [rate=" + rate + ", time=" + time + "]";
	}
}
