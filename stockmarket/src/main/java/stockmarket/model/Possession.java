package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * posession class
 * @author Damian Horna
 *
 */
public class Possession implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -8609584533467420915L;
	/**
	 * type of asset that is a possession
	 */
	private Asset assetType;
	/**
	 * quantity of the asset
	 */
	private BigDecimal quantity;
	public Asset getAssetType() {
		return assetType;
	}
	public void setAssetType(Asset assetType) {
		this.assetType = assetType;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public Possession(Asset assetType, BigDecimal quantity) {
		super();
		this.assetType = assetType;
		this.quantity = quantity;
	}
	
	
}
