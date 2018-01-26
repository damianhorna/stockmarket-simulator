package stockmarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * investment fund class
 * @author Damian Horna
 *
 */
public class InvestmentFund extends Gambler{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 5100509841917637034L;
	/**
	 * name of investment fund
	 */
	private String name;
	/**
	 * name of CEO of investment fund
	 */
	private String nameOfCEO;
	/**
	 * surname of CEO of investment fund
	 */
	private String surnameOfCEO;
	/**
	 * number of shares of investment fund
	 */
	private int numOfInvestmentFundShares;
	/**
	 * value of one investment fund share
	 */
	private BigDecimal investmentFundSharesValue;
	/**
	 * list of shareholders of investment fund shares
	 */
	private List<Investor> investmentFundSharesOwners = new ArrayList<>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameOfCEO() {
		return nameOfCEO;
	}
	public void setNameOfCEO(String nameOfCEO) {
		this.nameOfCEO = nameOfCEO;
	}
	public String getSurnameOfCEO() {
		return surnameOfCEO;
	}
	public void setSurnameOfCEO(String surnameOfCEO) {
		this.surnameOfCEO = surnameOfCEO;
	}
	public int getNumOfInvestmentFundShares() {
		return numOfInvestmentFundShares;
	}
	public void setNumOfInvestmentFundShares(int numOfInvestmentFundShares) {
		this.numOfInvestmentFundShares = numOfInvestmentFundShares;
	}
	public BigDecimal getInvestmentFundSharesValue() {
		return investmentFundSharesValue;
	}
	public void setInvestmentFundSharesValue(BigDecimal investmentFundSharesValue) {
		this.investmentFundSharesValue = investmentFundSharesValue;
	}
	public List<Investor> getInvestmentFundSharesOwners() {
		return investmentFundSharesOwners;
	}
	public void setInvestmentFundSharesOwners(List<Investor> investmentFundSharesOwners) {
		this.investmentFundSharesOwners = investmentFundSharesOwners;
	}

	public InvestmentFund(BigDecimal budget, List<Possession> assets, String name, String nameOfCEO, String surnameOfCEO,
			int numOfInvestmentFundShares, BigDecimal investmentFundSharesValue,
			List<Investor> investmentFundSharesOwners) {
		super(budget, assets);
		this.name = name;
		this.nameOfCEO = nameOfCEO;
		this.surnameOfCEO = surnameOfCEO;
		this.numOfInvestmentFundShares = numOfInvestmentFundShares;
		this.investmentFundSharesValue = investmentFundSharesValue;
		this.investmentFundSharesOwners = investmentFundSharesOwners;
	}
	public InvestmentFund(String iName, String iNameOfCEO, String iSurnameOfCEO, String iBudget) {
		this(new BigDecimal(iBudget), new ArrayList<>(), iName, iNameOfCEO, iSurnameOfCEO,0,BigDecimal.ZERO, new ArrayList<>());
	}
	@Override
	public String toString() {
		return "InvestmentFund [name=" + name + ", nameOfCEO=" + nameOfCEO + ", surnameOfCEO=" + surnameOfCEO
				+ ", numOfInvestmentFundShares=" + numOfInvestmentFundShares + ", investmentFundSharesValue="
				+ investmentFundSharesValue + ", investmentFundSharesOwners=" + investmentFundSharesOwners + "]";
	}
	@Override
	public void invest() {
		//buy some shares, currencies, raw materials just as investor
	}
	
	/**
	 * calculates the value of investment fund shares
	 */
	public void calculateValueOfInvestmentFundShares() {
	}
	@Override
	public void run() {		
		calculateValueOfInvestmentFundShares();
	}

	
}
