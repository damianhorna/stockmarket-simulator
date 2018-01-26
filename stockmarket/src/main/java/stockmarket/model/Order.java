package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * order class
 * @author Damian Horna
 *
 */
public class Order implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -7615058418082976800L;
	/**
	 * asset reference
	 */
	private Asset asset;
	/**
	 * list of investors that want to sell the asset
	 */
	private HashMap<Gambler, BigDecimal> sellers;
	/**
	 * list of investors that want to buy the asset
	 */
	private HashMap<Gambler,BigDecimal> buyers;
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public HashMap<Gambler, BigDecimal> getSellers() {
		return sellers;
	}
	public void setSellers(HashMap<Gambler, BigDecimal> sellers) {
		this.sellers = sellers;
	}
	public Order() {
		super();
		sellers = new HashMap<>();
		buyers = new HashMap<>();
	}
	public Order(Asset asset, HashMap<Gambler, BigDecimal> sellers, HashMap<Gambler, BigDecimal> buyers) {
		super();
		this.asset = asset;
		this.sellers = sellers;
		this.buyers = buyers;
	}
	public HashMap<Gambler, BigDecimal> getBuyers() {
		return buyers;
	}
	public void setBuyers(HashMap<Gambler, BigDecimal> buyers) {
		this.buyers = buyers;
	}
	@Override
	public String toString() {
		return "Order [asset=" + asset + ", sellers=" + sellers + ", buyers=" + buyers + "]";
	}
	
	
}
