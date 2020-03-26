package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * market class
 * @author Damian Horna
 *
 */
public abstract class Market implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 9059432801031754227L;
	/**
	 * name of the market
	 */
	private String name;
	/**
	 * country of the market
	 */
	private String country;
	/**
	 * city of the market
	 */
	private String city;
	/**
	 * address of the market
	 */
	private String address;
	/**
	 * profit margin of the market
	 */
	private BigDecimal profitMargin;
	public Market(String name, String country, String city, String address, BigDecimal profitMargin) {
		super();
		this.name = name;
		this.country = country;
		this.city = city;
		this.address = address;
		this.profitMargin = profitMargin;
	}
	public String getAddress() {
		return address;
	}
	public String getCity() {
		return city;
	}
	public String getCountry() {
		return country;
	}
	public String getName() {
		return name;
	}
	public BigDecimal getProfitMargin() {
		return profitMargin;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProfitMargin(BigDecimal profitMargin) {
		this.profitMargin = profitMargin;
	}
	@Override
	public String toString() {
		return "Market [name=" + name + ", country=" + country + ", city=" + city + ", address=" + address
				+ ", profitMargin=" + profitMargin + "]";
	}

}
