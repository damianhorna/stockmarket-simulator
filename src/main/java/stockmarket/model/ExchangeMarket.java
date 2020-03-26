package stockmarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import stockmarket.utils.PseudoDB;

/**
 * exchange market class
 * @author Damian Horna
 *
 */
public class ExchangeMarket extends Market {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 5370883132152196090L;
	private List<Currency> quotedCurrencies = new ArrayList<>();

	public List<Currency> getQuotedCurrencies() {
		return quotedCurrencies;
	}

	public void setQuotedCurrencies(List<Currency> quotedCurrencies) {
		this.quotedCurrencies = quotedCurrencies;
	}

	public ExchangeMarket(String name, String country, String city, String address, BigDecimal profitMargin) {
		super(name, country, city, address, profitMargin);
	}

	/**
	 * responsible for adding new currencies to the exchange market
	 * @param curr currency to be added
	 */
	public void addCurrency(Currency curr) {
		this.getQuotedCurrencies().add(curr);
		curr.initialize();
		curr.setQuoted(true);
		curr.setMarket(this);
	}

	/**
	 * removes given currency from the market
	 * @param currency currency to be removed from the market
	 */
	public void removeCurrency(Currency currency) {
		for (Gambler g : PseudoDB.getInvestors()) {
			try {
				Possession pos = g.getPossessions().stream().filter(p -> p.getAssetType().equals(currency))
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
				Possession pos = g.getPossessions().stream().filter(p -> p.getAssetType().equals(currency))
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
				.filter(o -> !o.getAsset().getName().equals(currency.getName())).collect(Collectors.toList()));

		currency.setQuoted(false);
	}
}
