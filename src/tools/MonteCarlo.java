package tools;

public class MonteCarlo {

	//NOTE THIS ONLY ACCOUNTS FOR INCREASE IN STOCK PRICE AND NO TIME VALUE LEAKING OR IV INCREASING
	
	/**
	 * 
	 * @param nStep - how many steps to predict
	 * @param STOCK
	 * @param STRIKE
	 * @param RATE
	 * @param TIME - in years
	 * @param VOLATILITY
	 * @param DIV
	 * @param RETURN - Only exercise option when STOCK*(RETURN+1)-STRIKE>0
	 * @return
	 */
	public static double generatePayoffs(int nStep,double STOCK,double STRIKE,double RATE, double TIME, double VOLATILITY,double DIV,double RETURN) {
		int epsilon; //Error term
		double stockPrice = STOCK;
		int n=0;
		while(n<nStep) {
			if(Math.random()>0.5) {
				epsilon=1;
			}
			else {
				epsilon = -1;
			}
			stockPrice = stockPrice + RATE*stockPrice*(TIME/nStep) + VOLATILITY*Math.sqrt(TIME/nStep)*epsilon*stockPrice;
			n++;
			//Check if stockPrice suffices RETURN arguments and exercise our rights
			//Exercise our right when stock STOCK-STRIKE has increased by RETURN
			if(stockPrice-STRIKE>-RETURN*(STOCK-STRIKE)) {
				System.out.println(stockPrice);
				return stockPrice-STRIKE;
			}
		}
		//If stockPrice never suffices RETURN arguments then no payoff because we don't exercise our rights
		return Math.max(stockPrice-STRIKE, 0);
		
	}
	public static double[] generateStockPrices(int nStep,double STOCK,double RATE, double TIME, double VOLATILITY,double DIV) {
		int epsilon; //Error term
		double stockPrice = STOCK;
		double stockPrices[] = new double[nStep];
		int n=0;
		while(n<nStep) {
			if(Math.random()>0.5) {
				epsilon=1;
			}
			else {
				epsilon = -1;
			}
			stockPrice = stockPrice + RATE*stockPrice*(TIME/nStep) + VOLATILITY*Math.sqrt(TIME/nStep)*epsilon*stockPrice;
			stockPrices[n] = stockPrice;
			n++;
		}
		return stockPrices;
	}
}
