package stockmarket.simulation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import stockmarket.model.Company;
import stockmarket.model.RatingRecord;
import stockmarket.model.StockExchangeIndex;
import stockmarket.utils.PseudoDB;

/**
 * class that updates ratings of all indexes
 * @author Damian Horna
 *
 */
public class IndexQuotingWriter implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			List<StockExchangeIndex> indexes = PseudoDB.getStockExchangeIndexes();
			List<Company> allCompanies = PseudoDB.getCompanies();
			for (StockExchangeIndex sei : indexes) {
				String sector = sei.getCondition();
				if(!sei.getCondition().equals("CUSTOMIZED")) {
					sei.getCompanies().clear();
				}
				for (Company c : allCompanies) {
					if (c.getEconomicSector().equals(sector) && !sei.getCondition().equals("CUSTOMIZED") && c.isQuoted()
							&& (PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getName()))
									.collect(Collectors.toList()).get(0).getMarket().equals(sei.getStockExchange()))) {
						
							sei.getCompanies().add(c);
						
					}
				}

				List<Company> companies = sei.getCompanies();
				BigDecimal val = BigDecimal.ZERO;
				for (Company c : companies) {
					val = val.add(PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getName()))
							.collect(Collectors.toList()).get(0).getCurrentRate());
				}
				if (sei.getInitial().compareTo(BigDecimal.ZERO) <= 0) {
					sei.setInitial(val);
				}
				sei.setValue(val);
				if (sei.getInitial().compareTo(BigDecimal.ZERO) <= 0) {
					sei.setChange(BigDecimal.ZERO);
				} else {
					sei.setChange((sei.getValue().divide(sei.getInitial(),2).multiply(new BigDecimal(100))).subtract(new BigDecimal(100)));
				}

				RatingRecord ratRec = new RatingRecord(sei.getValue(), new Date(), BigDecimal.ZERO);
				if (!(sei.getValue().compareTo(BigDecimal.ZERO) <= 0)) {
					sei.getRatingRecords().add(ratRec);
				}
				sei.getRatingRecords().add(ratRec);

			}
			try {
				System.out.println("***index rewriter***");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
