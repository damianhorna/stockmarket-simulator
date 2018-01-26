package stockmarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import stockmarket.utils.PseudoDB;


/**
 * raw materials market class
 * @author Damian Horna
 *
 */
public class RawMaterialsMarket extends Market{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 2628052157161519705L;
	/**
	 * list of quoted raw materials on the market
	 */
	private List<RawMaterial> quotedRawMaterials = new ArrayList<>();

	public RawMaterialsMarket(String name, String country, String city, String address, BigDecimal profitMargin) {
		super(name, country, city, address, profitMargin);
	}

	public List<RawMaterial> getQuotedRawMaterials() {
		return quotedRawMaterials;
	}

	public void setQuotedRawMaterials(List<RawMaterial> rawMaterialsPrices) {
		this.quotedRawMaterials = rawMaterialsPrices;
	}
	
	/**
	 * adds raw material to the market
	 * @param material material to be added
	 */
	public void addMaterial(RawMaterial material) {
		this.getQuotedRawMaterials().add(material);
		material.initialize();
		material.setQuoted(true);
		material.setMarket(this);
	}

	/**
	 * removes raw material from the market
	 * @param material material to be removed
	 */
	public void removeRawMaterial(RawMaterial material) {
		for (Gambler g : PseudoDB.getInvestors()) {
			try {
				Possession pos = g.getPossessions().stream().filter(p -> p.getAssetType().equals(material))
						.collect(Collectors.toList()).get(0);
				g.getPossessions().remove(pos);
				g.setOperationalBudget(
						g.getOperationalBudget().add(pos.getQuantity().multiply(pos.getAssetType().getCurrentRate())));
				g.setBudget(g.getBudget().add(pos.getQuantity().multiply(pos.getAssetType().getCurrentRate())));

			} catch (Exception e) {
				// the specific gambler doesn't have that item
			}

		}

		for (Gambler g : PseudoDB.getInvestmentFunds()) {
			try {
				Possession pos = g.getPossessions().stream().filter(p -> p.getAssetType().equals(material))
						.collect(Collectors.toList()).get(0);
				g.getPossessions().remove(pos);
				g.setOperationalBudget(
						g.getBudget().add(pos.getQuantity().multiply(pos.getAssetType().getCurrentRate())));
				g.setBudget(g.getBudget().add(pos.getQuantity().multiply(pos.getAssetType().getCurrentRate())));

			} catch (Exception e) {
				// the specific gambler doesn't have that item
			}

		}

		PseudoDB.setOrders(PseudoDB.getOrders().stream()
				.filter(o -> !o.getAsset().getName().equals(material.getName())).collect(Collectors.toList()));

		material.setQuoted(false);
	}
}
