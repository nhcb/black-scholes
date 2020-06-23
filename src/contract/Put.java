package contract;

import tools.Gaussian;
import tools.MonteCarlo;

public class Put extends Contract {

	public Put(double STOCK, double STRIKE, double RATE, double TIME, double VOLATILITY, double DIV) {
		super(STOCK, STRIKE, RATE, TIME, VOLATILITY, DIV);
	}

	@Override
	public void calculateBS(){
        //Option price
    	PRICE = STRIKE * Gaussian.cdf(-d2()) - STOCK * Math.exp(-RATE *TIME) * Gaussian.cdf(-d1());
        //Greeks
        DELTA = Gaussian.cdf(d1())-1;
        //CASH GAMMA
        GAMMA = Gaussian.pdf(d1())/ (STOCK*VOLATILITY*Math.sqrt(TIME)) ; //STOCK^2
        
        THETA = (-(STOCK * Gaussian.pdf(d1()) * VOLATILITY)) / (2 * Math.sqrt(TIME)) + (RATE * STRIKE * Math.exp(-RATE * TIME) * Gaussian.cdf(-d2()));
        VEGA = STOCK * Gaussian.pdf(d1())*Math.sqrt(TIME);
        RHO =  -STRIKE*TIME*Math.exp(-RATE*TIME)*Gaussian.cdf(-d2());
       
    }
	 @Override
	    public void calculateMC(int nStep,int nSim) {
	    	int n=0;
	    	double totalPayoff=0;
	    	double averagePayoff;
	    	while(n < nSim) {
	    		//Exercise our right when stock STOCK-STRIKE has increased by RETURN
	    		totalPayoff += MonteCarlo.generatePayoffs(nStep, STOCK, STRIKE, RATE, TIME, VOLATILITY, DIV, 0.1);
	    		n++;
	    	}
	    	averagePayoff = totalPayoff/nSim;
	    	//Calculate option value PRESENT VALUE
	    	PRICE = averagePayoff;///(Math.pow(1+RATE,TIME));
	    }
    private double d1() {
        return (Math.log(STOCK / STRIKE) + ((Math.pow(VOLATILITY, 2) * TIME) / 2)) / (Math.pow(TIME, 0.5) * VOLATILITY);
    }
    private double d2() {
        return d1() - (VOLATILITY * Math.pow(TIME, 0.5));
    }
}
