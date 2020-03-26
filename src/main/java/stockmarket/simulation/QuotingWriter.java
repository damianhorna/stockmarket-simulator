package stockmarket.simulation;

import java.util.ArrayList;
import java.util.List;

import stockmarket.model.Asset;
import stockmarket.utils.PseudoDB;

/**
 * class that updates the ratings of all assets
 * @author Damian Horna
 *
 */
public class QuotingWriter implements Runnable {

	
	List<Asset> allAssets = new ArrayList<>();
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
		try {
			allAssets.addAll(PseudoDB.getCurrencies());
			allAssets.addAll(PseudoDB.getShares());
			allAssets.addAll(PseudoDB.getRawMaterials());
			for (Asset a : allAssets) {
				a.setInitialRate(a.getCurrentRate());
				a.setMaximalRate(a.getCurrentRate());
				a.setMinimalRate(a.getCurrentRate());
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			//
		}
		}
	}

}
