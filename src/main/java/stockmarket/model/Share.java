package stockmarket.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * share class
 * @author Damian Horna
 *
 */
public class Share extends Asset{
	/**
	 * id for serialization
	 */
	private static final long serialVersionUID = -9214801908359370923L;
	/**
	 * company of share
	 */
	private Company company;

	public Share(String companyName, Company comp) {
		super(companyName);
		this.company=comp;
	}


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}


	@Override
	public void buy() {
		
	}

	@Override
	public void sell() {
		
	}


	@Override
	public void initialize() {
		BigDecimal init = this.company.getShareCapital().divide(this.company.getNumOfShares(),2,RoundingMode.HALF_UP);
		
		this.setFirstAppraisalDate(new Date());
		this.setAllTimeHigh(init);
		this.setAllTimeLow(init);
		this.setInitialRate(init);
		this.setMinimalRate(init);
		this.setCurrentRate(init);
		this.setMaximalRate(init);
		this.setChange(BigDecimal.ZERO);
		this.setChangePct(BigDecimal.ZERO);
		RatingRecord ratingRecord = new RatingRecord(init,new Date(),BigDecimal.ZERO);
		this.getRatingRecords().add(ratingRecord);
	}
}
