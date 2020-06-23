package application;

import java.net.URL;
import java.util.ResourceBundle;

import contract.Call;
import contract.Contract;
import contract.Put;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class MainController implements Initializable {
	//JAVAFX Input values
	String contractType;
	double strike;
	double underlying;
	double volatility;
	double time;
	
	//Calculated values
	double optionValue;
	double delta;
	double theta;
	
    @FXML
    private LineChart<Number, Number> OVU;
    
    @FXML
    private ComboBox<String> contractBox;
    
    ObservableList<String> list = FXCollections.observableArrayList("Call","Put");
    @FXML
    public void onContractChanged(ActionEvent event) {
    	contractType = contractBox.getValue();
    	//Create new contract
    	System.out.println(contractType);
    	//Calculate new series
    	calculcate(contractType, underlying, strike, time, volatility);
    }
    @FXML
    private Slider strikeSlider;

    @FXML
    void onStrikeSliderChanged(MouseEvent event) {
    	strike = strikeSlider.getValue();
    	//Calculate new series
    	calculcate(contractType, underlying, strike, time, volatility);
    }

    @FXML
    private Slider ivSlider;

    @FXML
    void onIVSliderChanged(MouseEvent event) {
    	volatility = ivSlider.getValue();
    	//Calculate new series
    	calculcate(contractType, underlying, strike, time, volatility);
    }
    
    @FXML
    private Slider timeSlider;

    @FXML
    void onTimeSliderChanged(MouseEvent event) {
    	time = timeSlider.getValue()/365;
    	//Calculate new series
    	calculcate(contractType, underlying, strike, time, volatility);
    }
    
    @FXML
    private Slider underlyingSlider;


    @FXML
    void onUnderlyingChanged(MouseEvent event) {
    	underlying = underlyingSlider.getValue();
    	//Calculate new series
    	calculcate(contractType, underlying, strike, time, volatility);
    }
    
    @FXML
    private LineChart<Number, Number> OVT;

    @FXML
    private LineChart<Number, Number> DT;

    @FXML
    private LineChart<Number, Number> TT;
    
    @FXML
    private LineChart<Number, Number> PROFIT;
    
    @FXML
    private LineChart<Number, Number> MonteCarlo;
    
    
    @FXML
    private NumberAxis thetaX;
    
    @FXML
    private NumberAxis thetaY;

    public void calculcate(String contractType,double underlying,double strike,double time,double volatility) {
    	//Clear all graphs
    	OVU.getData().clear();
    	DT.getData().clear();
    	OVT.getData().clear();
    	TT.getData().clear();
    	PROFIT.getData().clear();
    	MonteCarlo.getData().clear();
    	//Get data from graphs
    	
    	//Initialize series
    	XYChart.Series<Number, Number> seriesValue = new XYChart.Series<Number, Number>();
    	XYChart.Series<Number, Number> seriesDelta = new XYChart.Series<Number, Number>();
    	
    	XYChart.Series<Number, Number> seriesValueTime = new XYChart.Series<Number, Number>();
    	XYChart.Series<Number, Number> seriesThetaTime = new XYChart.Series<Number, Number>();
    	
    	XYChart.Series<Number, Number> seriesProfit = new XYChart.Series<Number, Number>();
    	
    	
    	//Call
    	if(contractType.equals("Call")) {
    		//Calculate cost
    		Contract cost = new Call(underlying,strike,0,time,volatility,0);
    		cost.calculateBS();
    		double costOption = cost.getOptionValue();
    		cost.calculateMC(400, 10000);
    		System.out.println("Black-Scholes: " + costOption + "\n"+
    							"Monte-Carlo: " + cost.getOptionValue());
    		
    		//Generate stock prices
    		double stockPrices[][] = cost.generateStockPricesMC(50, 250);
    		System.out.println(time*365);
    		//Copy them in graph
    		for(int i = 0; i < 250; i ++) {
    			XYChart.Series<Number, Number> seriesMC = new XYChart.Series<Number, Number>();
    			for(int j = 0; j < 50;j++) {
    				seriesMC.getData().add(new XYChart.Data<Number, Number>((j+1)*time*365/50, stockPrices[j][i]));
    			}
    			MonteCarlo.getData().add(seriesMC);
    		}
    		
    		for(double i = 0; i <= underlying + 25; i ++) {
        		Contract call = new Call(i,strike,0,time,volatility,0);
        		call.calculateBS();
        		//Get relevant values
        		optionValue = call.getOptionValue();
        		delta = call.getDelta();
        		
        		//Add values to graph
        		seriesValue.getData().add(new XYChart.Data<Number, Number>(i, optionValue));
        		seriesDelta.getData().add(new XYChart.Data<Number, Number>(i, delta));
        		//100 contracts
        		seriesProfit.getData().add(new XYChart.Data<Number, Number>(i, (optionValue-costOption)*100));
    		}
    		for(double j = 0 ; j < Math.round(time*365); j++) {
    			Contract call = new Call(underlying,strike,0,time-(double)j/365,volatility,0);
        		call.calculateBS();
        		//Get relevant values
        		optionValue = call.getOptionValue();
        		theta = call.getTheta();
        		
        		//Add values to graph
        		seriesValueTime.getData().add(new XYChart.Data<Number, Number>(j, optionValue));
        		seriesThetaTime.getData().add(new XYChart.Data<Number, Number>(j, theta));
    		}
    	}
    	//Put
    	else {
    		//Calculate cost
    		Contract cost = new Put(underlying,strike,0,time,volatility,0);
    		cost.calculateBS();
    		double costOption = cost.getOptionValue();
    		cost.calculateMC(400, 10000);
    		System.out.println("Black-Scholes: " + costOption + "\n"+
    							"Monte-Carlo: " + cost.getOptionValue());
    		//Generate stock prices
    		double stockPrices[][] = cost.generateStockPricesMC(50, 250);
    		//Copy them in graph
    		for(int i = 0; i < 250; i ++) {
    			XYChart.Series<Number, Number> seriesMC = new XYChart.Series<Number, Number>();
    			for(int j = 0; j < 50;j++) {
    				seriesMC.getData().add(new XYChart.Data<Number, Number>((j+1)*time*365/50, stockPrices[j][i]));
    			}
    			MonteCarlo.getData().add(seriesMC);
    		}
    		
    		
    		for(double i = 0; i <= underlying + 25; i ++) {
        		Contract put = new Put(i,strike,0,time,volatility,0);
        		put.calculateBS();
        		//Get relevant values
        		optionValue = put.getOptionValue();
        		delta = put.getDelta();
        		
        		//Add values to graph
        		seriesValue.getData().add(new XYChart.Data<Number, Number>(i, optionValue));
        		seriesDelta.getData().add(new XYChart.Data<Number, Number>(i, delta));
        		//100 contracts
        		seriesProfit.getData().add(new XYChart.Data<Number, Number>(i, (optionValue-costOption)*100));
    		}
    		for(double j = 0 ; j < Math.round(time*365); j++) {
    			Contract put = new Put(underlying,strike,0,time-(double)j/365,volatility,0);
        		put.calculateBS();
        		//Get relevant values
        		optionValue = put.getOptionValue();
        		theta = put.getTheta();
        		
        		//Add values to graph
        		seriesValueTime.getData().add(new XYChart.Data<Number, Number>(j, optionValue));
        		seriesThetaTime.getData().add(new XYChart.Data<Number, Number>(j, theta));
    		}
    		
    	}
    	
    	
    	
    	
    	OVU.getData().add(seriesValue);
    	DT.getData().add(seriesDelta);
    	OVT.getData().add(seriesValueTime);
    	TT.getData().add(seriesThetaTime);
    	PROFIT.getData().add(seriesProfit);
    	
    	OVU.setLegendVisible(false);
    	DT.setLegendVisible(false);
    	OVT.setLegendVisible(false);
    	TT.setLegendVisible(false);
    	PROFIT.setLegendVisible(false);
    	MonteCarlo.setLegendVisible(false);
    	
    	//hide dots
    	OVU.setCreateSymbols(false);
    	DT.setCreateSymbols(false);
    	OVT.setCreateSymbols(false);
    	TT.setCreateSymbols(false);
    	PROFIT.setCreateSymbols(false);
    	MonteCarlo.setCreateSymbols(false);
    }


    @Override
    public void initialize(URL location,ResourceBundle resources) {
    	contractBox.setItems(list);
    	contractBox.setValue("Call");
    	
    	//Get inputs
    	time = (double)timeSlider.getValue()/365;
    	underlying = underlyingSlider.getValue();
    	volatility = ivSlider.getValue();
    	strike = strikeSlider.getValue();
    	contractType = contractBox.getValue();
    	

    	//calculate
    	calculcate(contractType, underlying, strike, time, volatility);
    	
    }
}
