package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * gambler abstract class
 * @author Damian Horna
 *
 */
public abstract class Gambler implements Serializable, Runnable {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 139313273642631849L;
	/**
	 * budget of a gambler
	 */
	private BigDecimal budget;
	/**
	 * operational budget of a gambler
	 */
	private BigDecimal operationalBudget;
	/**
	 * list of all posessions of a gambler
	 */
	private List<Possession> possessions;

	public Gambler(BigDecimal budget, List<Possession> assets) {
		super();
		this.budget = budget;
		this.possessions = assets;
		this.operationalBudget = budget;
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public BigDecimal getOperationalBudget() {
		return operationalBudget;
	}

	public List<Possession> getPossessions() {
		return possessions;
	}

	public abstract void invest();

	@Override
	public abstract void run();

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public void setOperationalBudget(BigDecimal operationalBudget) {
		this.operationalBudget = operationalBudget;
	}

	public void setPossessions(List<Possession> assets) {
		this.possessions = assets;
	}

	@Override
	public String toString() {
		return "Gambler [budget=" + budget + ", assets=" + possessions + "]";
	}
}
