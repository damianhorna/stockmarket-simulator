package stockmarket.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import stockmarket.utils.PseudoDB;

/**
 * company class
 * @author Damian Horna
 *
 */
public class Company implements Serializable, Runnable {
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = 8113588839723961788L;
	/**
	 * name of the company
	 */
	private String name;
	/**
	 * number of shares of the company 
	 */
	private BigDecimal numOfShares;
	/**
	 * list of all shareholders
	 */
	private List<Gambler> shareholders = new ArrayList<>();
	/**
	 * profit a company made
	 */
	private BigDecimal profit;
	/**
	 * income a company made
	 */
	private BigDecimal income;
	/**
	 * equity capital
	 */
	private BigDecimal equityCapital;
	/**
	 * share capital
	 */
	private BigDecimal shareCapital;
	/**
	 * turnover
	 */
	private BigDecimal turnover;
	/**
	 * economic sector of a company
	 */
	private String economicSector;
	/**
	 * informs whether the company is quoted or not
	 */
	private boolean isQuoted;
	/**
	 * share of the company
	 */
	private Share share;

	public Company(String name, BigDecimal numOfShares, List<Gambler> shareholders, BigDecimal profit,
			BigDecimal income, BigDecimal equityCapital, BigDecimal shareCapital, BigDecimal turnover,
			String economicSector) {
		super();
		this.name = name;
		this.numOfShares = numOfShares;
		this.shareholders = shareholders;
		this.profit = profit;
		this.income = income;
		this.equityCapital = equityCapital;
		this.shareCapital = shareCapital;
		this.turnover = turnover;
		this.economicSector = economicSector;
		this.isQuoted = false;
		this.share = new Share(this.getName(), this);
		PseudoDB.getShares().add(share);
	}

	public Company(String cName, String cSector) {
		this(cName, BigDecimal.ZERO, new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, cSector);
	}

	public String getEconomicSector() {
		return economicSector;
	}

	public BigDecimal getEquityCapital() {
		return equityCapital;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getNumOfShares() {
		return numOfShares;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public BigDecimal getShareCapital() {
		return shareCapital;
	}

	public List<Gambler> getShareholders() {
		return shareholders;
	}

	public BigDecimal getTurnover() {
		return turnover;
	}

	public boolean isQuoted() {
		return isQuoted;
	}

	public void setEconomicSector(String economicSector) {
		this.economicSector = economicSector;
	}

	public void setEquityCapital(BigDecimal equityCapital) {
		this.equityCapital = equityCapital;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumOfShares(BigDecimal numOfShares) {
		this.numOfShares = numOfShares;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public void setQuoted(boolean isQuoted) {
		this.isQuoted = isQuoted;
	}

	public void setShareCapital(BigDecimal shareCapital) {
		this.shareCapital = shareCapital;
	}

	public void setShareholders(List<Gambler> shareholders) {
		this.shareholders = shareholders;
	}

	public void setTurnover(BigDecimal turnover) {
		this.turnover = turnover;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", numOfShares=" + numOfShares + ", shareholders=" + shareholders + ", profit="
				+ profit + ", income=" + income + ", equityCapital=" + equityCapital + ", shareCapital=" + shareCapital
				+ ", turnover=" + turnover + ", economicSector=" + economicSector + ", isQuoted=" + isQuoted + "]";
	}

	// simulation stuff

	/**
	 * issues an IPO of a company
	 */
	public void issueInitialPublicOffering() {
		if (PseudoDB.getStockExchanges().size() > 0) {
			StockExchange se = PseudoDB.getStockExchanges()
					.get(ThreadLocalRandom.current().nextInt(0, PseudoDB.getStockExchanges().size()));
			se.getCompanyList().add(this);
			isQuoted = true;
			share.setQuoted(true);
			share.setMarket(se);
			PseudoDB.getStockExchangeIndexes().stream().filter(e -> e.getName().equals(this.getEconomicSector()))
					.forEach(l -> l.getCompanies().add(this));
			// random number of shares
			this.numOfShares = new BigDecimal(ThreadLocalRandom.current().nextInt(200000, 500001));
			this.shareCapital = new BigDecimal(ThreadLocalRandom.current().nextInt(100000, 300001));
			this.equityCapital = shareCapital.add(new BigDecimal(ThreadLocalRandom.current().nextInt(100000, 200000)));
			share.initialize();

		}
	}

	/**
	 * issues new random number of shares
	 */
	public void issueNewShares() {
		this.numOfShares.add(new BigDecimal(ThreadLocalRandom.current().nextInt(50000, 100001)));
	}

	/**
	 * responsible for buying own shares by the company
	 * @param bdQuan how many shares to buy
	 * @param bdPrice price of one share
	 */
	public void buyShares(BigDecimal bdQuan, BigDecimal bdPrice) {
		BigDecimal current = PseudoDB.getShares().stream().filter(s -> s.getCompany().equals(this))
				.collect(Collectors.toList()).get(0).getCurrentRate();
		BigDecimal fraction = current.divide(this.getNumOfShares(), 2);
		PseudoDB.getShares().stream().filter(s -> s.getCompany().equals(this)).collect(Collectors.toList()).get(0)
				.setCurrentRate(current.add(bdQuan.multiply(fraction)));
		this.setNumOfShares(this.getNumOfShares().subtract(bdQuan));
	}

	/**
	 * generates random income and profit
	 */
	public void generateIncomeAndProfit() {
		this.income = (new BigDecimal(ThreadLocalRandom.current().nextInt(50000, 100001)));
		this.profit = income.divide(new BigDecimal(ThreadLocalRandom.current().nextInt(2, 4)), 2, RoundingMode.HALF_UP);
	}

	@Override
	public void run() {
		while (true) {
			if (ThreadLocalRandom.current().nextInt(1, 11) == 3 && !isQuoted) {
				issueInitialPublicOffering();
			}
			// calculate turnover?
			generateIncomeAndProfit();
			if (ThreadLocalRandom.current().nextInt(1, 11) == 7 && isQuoted) {
				issueNewShares();
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
