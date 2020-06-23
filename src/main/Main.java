package main;

import contract.Contract;
import contract.Put;
import contract.Call;
import contract.CallSpread;

public class Main  {
    public static void main (String args[]) {
    	double underlying = 100;
    	double callPrice = 105;
    	double putPrice = 95;
    	double volatility = 0.40;
    	double rate = 0;
    	double time = (double) 28/365; 
    	double div = 0;
       Contract callExample = new Call(underlying,callPrice,rate,time,volatility,div);
       callExample.calculateBS();
       Contract putExample = new Put(underlying,putPrice,rate,time,volatility,div);
       putExample.calculateBS();
       Contract callSpreadExample = new CallSpread(underlying,putPrice,callPrice,rate,time,volatility,div);
       callSpreadExample.calculateBS();
       
       System.out.println("Call: ");
       System.out.println(callExample.toString());
       System.out.println("Put: ");
       System.out.println(putExample.toString());
       System.out.println("Call Spread: ");
       System.out.println(callSpreadExample.toString());
    }
}
