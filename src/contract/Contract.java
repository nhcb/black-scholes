package contract;

import tools.MonteCarlo;

public class Contract {
	
    protected double STOCK;
    protected double STRIKE;
    protected double RATE;
    protected double TIME;
    protected double VOLATILITY;
    //protected double FORWARD;
    protected double DIV; //Dividend Yield

    protected double PRICE;
    protected double DELTA;
    protected double GAMMA;
    protected double THETA;
    protected double VEGA;
    protected double RHO;

    /**
     *
     * @param STOCK
     * @param STRIKE
     * @param RATE
     * @param TIME
     * @param VOLATILITY
     * @param DIV
     */
    public Contract(double STOCK, double STRIKE, double RATE, double TIME, double VOLATILITY,double DIV){
        this.STOCK = STOCK;
        this.STRIKE = STRIKE;
        this.RATE = RATE;
        this.TIME = TIME;
        this.VOLATILITY = VOLATILITY;
    }
	public void calculateBS(){

    }
	public void calculateMC(int nStep,int nSim) {

	}
	public double[][] generateStockPricesMC(int nStep,int nSim){
		int n=0;
    	double data[][]= new double[nStep][nSim];
    	while(n < nSim) {
    		//Exercise our right when stock STOCK-STRIKE has increased by RETURN
    		double col[] = MonteCarlo.generateStockPrices(nStep, STOCK, RATE, TIME, VOLATILITY, DIV);
    		for(int i =0;i < nStep;i ++) {
    			data[i][n] = col[i];
    		}
    		n++;
    	}
    	return data;
	}
    public double getOptionValue() {
        return PRICE;
    }

    public void setOptionValue(double optionValue) {
        this.PRICE = optionValue;
    }

    public double getDelta() {
        return DELTA;
    }

    public void setDelta(double delta) {
        this.DELTA = delta;
    }

    public double getTheta() {
    	//Divided by 365 per calender days in year
        return THETA/365;
    }

    public void setTheta(double theta) {
        this.THETA = theta;
    }

    public double getRho() {
    	//Divided by 1 00 as a 1 basis point rate change
        return RHO/100;
    }

    public void setRho(double rho) {
        this.RHO = rho;
    }

    public double getGamma() {
        return GAMMA;
    }

    public void setGamma(double gamma) {
        this.GAMMA = gamma;
    }

    public double getVega() {
    	//Divided by 100 as a 1 vol point change
        return VEGA/100;
    }

    public void setVega(double vega) {
        this.VEGA = vega;
    }
    public String toString() {
		return "Underlying: " + STOCK + "\n"+
				"Strike: " + STRIKE + "\n"+
				"Time: " + Math.round(TIME*365) + "\n"+
				"Option value: " + this.getOptionValue() + "\n"+
				"Delta: " + this.getDelta() + "\n"+
				"Gamma: " + this.getGamma() + "\n"+
				"Theta: " + this.getTheta() + "\n"+
				"Vega: " + this.getVega() + "\n"+
				"Rho: " + this.getRho();
    	
    }
}
