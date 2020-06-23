package contract;


public class CallSpread extends Contract {

	private double STRIKE2;
	
	public CallSpread(double STOCK, double STRIKE, double RATE, double TIME, double VOLATILITY, double DIV) {
		super(STOCK, STRIKE, RATE, TIME, VOLATILITY, DIV);
		//Standard bullish call spread
		this.STRIKE2 = STRIKE+5;
	}
	public CallSpread(double STOCK, double STRIKE1,double STRIKE2, double RATE, double TIME, double VOLATILITY, double DIV) {
		super(STOCK, STRIKE1, RATE, TIME, VOLATILITY, DIV);
		this.STRIKE2 = STRIKE2;
	}
	@Override
	public void calculateBS() {
		//TODO:Determine bullish or bearish call spread
		Contract buy = new Call(STOCK, STRIKE, RATE, TIME, VOLATILITY, DIV);
		Contract sell = new Call(STOCK, STRIKE2, RATE, TIME, VOLATILITY, DIV);
		buy.calculateBS();
		sell.calculateBS();
		 //Spread price
    	PRICE = buy.getOptionValue()-sell.getOptionValue();
        //Greeks
        DELTA = buy.getDelta()-sell.getDelta();
        //CASH GAMMA
        GAMMA = buy.getGamma()-sell.getGamma();
        
        THETA = buy.getTheta()-sell.getTheta();
        VEGA = buy.getVega()-sell.getVega();
        RHO =  buy.getRho()-sell.getRho();
	}
	@Override
	public String toString() {
		return "Underlying: " + STOCK + "\n"+
				"Strike (bought): " + STRIKE + "\n"+
				"Strike (sold): " + STRIKE2 + "\n"+
				"Time: " + Math.round(TIME*365) + "\n"+
				"Spread value: " + this.getOptionValue() + "\n"+
				"Delta: " + this.getDelta() + "\n"+
				"Gamma: " + this.getGamma() + "\n"+
				"Theta: " + this.getTheta() + "\n"+
				"Vega: " + this.getVega() + "\n"+
				"Rho: " + this.getRho();
		
	}
}
