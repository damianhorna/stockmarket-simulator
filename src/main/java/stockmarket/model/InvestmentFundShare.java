package stockmarket.model;

import java.io.Serializable;

/**
 * investment fund share class
 * @author Damian Horna
 *
 */
public class InvestmentFundShare implements Serializable{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 8237753922822439850L;
	private InvestmentFund fund;

	public InvestmentFundShare(InvestmentFund nameOfFund) {
		super();
		this.fund = nameOfFund;
	}

	public InvestmentFund getNameOfFund() {
		return fund;
	}

	public void setNameOfFund(InvestmentFund nameOfFund) {
		this.fund = nameOfFund;
	}

	@Override
	public String toString() {
		return "InvestmentFundShare [nameOfFund=" + fund + "]";
	}
	
}
