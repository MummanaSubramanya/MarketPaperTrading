package oibacktester;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.text.Position;

import org.json.JSONArray;
import org.json.JSONObject;

public class IndexToOptionMomentumCorrelation {
    
    static String startDate = "2023-03-08+09:15:00"; // yyyy-mm-dd - ZERODHA FORMAT
    static String endDate = "2023-03-08+15:15:00";
    static String expiryDate = "23309";
    static int candleSize = 1;
    static double optionDelta = 0.5;
    static String index = "NIFTY 50";
    static int qty = 50;
    static int INDEX_OPEN_SP = 0;
    static boolean placeLiveOrders = false;
    static boolean executeExitOrder = true;
    static boolean indexMomentumTotalLog = true;
    static double indexMomentumTotal = 0;

    // Logging for debug of orders placed
    static boolean printEachOrder = false;

    // Backtesting global variables
    static double totalPnlBacktest = 0;
    static int totalOrdersBacktest = 0;
   

    public static void main(String[] args) throws Exception{

        // int index = 0;
        // while(index < 60) {
        //     analyse();
        //     System.out.println(startDate + "," + getTotalPnl() + ","+totalOrdersBacktest);
        //     Positions.clearPositions(); totalOrdersBacktest = 0;
            
        //     startDate = updateTimeBy(startDate, 5);
        //     index++;
        // }



        // Actual Live Market Execution

        placeLiveOrders = false;
        executeExitOrder = false;
        indexMomentumTotalLog = true;

        // Logging for debug of orders placed
        printEachOrder = false;
        while(true) {
            analyse(); 
            printPositionInTableFormat();
            Thread.sleep(2000);
        }


        
    }
    public static void analyse() {
        try {
            String indexScriptCode = ZerodhaWrapper.getScriptCode(index);
            JSONArray indexTicks = getScriptData(indexScriptCode);
            double indexMomentumTotal = 0;

            String ce1ScriptName = "";
            String ce2ScriptName = "";
            String ce3ScriptName = "";
            String ce4ScriptName = "";
            String ce5ScriptName = "";
            String pe1ScriptName = "";
            String pe2ScriptName = "";
            String pe3ScriptName = "";
            String pe4ScriptName = "";
            String pe5ScriptName = "";


            double ce1Total = 0;
            double ce2Total = 0;
            double ce3Total = 0;
            double ce4Total = 0;
            double ce5Total = 0;
            double pe1Total = 0;
            double pe2Total = 0;
            double pe3Total = 0;
            double pe4Total = 0;
            double pe5Total = 0;

            int closePrice = indexTicks.getJSONArray(0).getInt(4);

            INDEX_OPEN_SP = closePrice;
            if(index.equalsIgnoreCase("NIFTY BANK")) {
                INDEX_OPEN_SP = ((INDEX_OPEN_SP + 50) / 100 ) * 100;
                ce1ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-400)/100*100) + "CE";
                ce2ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-300)/100*100)  +"CE";
                ce3ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-200)/100*100)  +"CE";
                ce4ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-100)/100*100)  +"CE";
                ce5ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP)/100*100)  +"CE";
                pe1ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+400)/100*100) +"PE";
                pe2ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+300)/100*100) +"PE";
                pe3ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+200)/100*100)  +"PE";
                pe4ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+100)/100*100)  +"PE";
                pe5ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP)/100*100)  +"PE";
            }else {
                INDEX_OPEN_SP = ((INDEX_OPEN_SP + 25) / 50 ) * 50;
                ce1ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-200)/50*50) + "CE";
                ce2ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-150)/50*50)  +"CE";
                ce3ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-100)/50*50)  +"CE";
                ce4ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-50)/50*50)  +"CE";
                ce5ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP)/50*50)  +"CE";
                pe1ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+200)/50*50) +"PE";
                pe2ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+150)/50*50) +"PE";
                pe3ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+100)/50*50)  +"PE";
                pe4ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+50)/50*50)  +"PE";
                pe5ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP)/50*50)  +"PE";
            }

            String ce1ScriptCode = ZerodhaWrapper.getScriptCode(ce1ScriptName);
            String ce2ScriptCode = ZerodhaWrapper.getScriptCode(ce2ScriptName);
            String ce3ScriptCode = ZerodhaWrapper.getScriptCode(ce3ScriptName);
            String ce4ScriptCode = ZerodhaWrapper.getScriptCode(ce4ScriptName);
            String ce5ScriptCode = ZerodhaWrapper.getScriptCode(ce5ScriptName);
            String pe1ScriptCode = ZerodhaWrapper.getScriptCode(pe1ScriptName);
            String pe2ScriptCode = ZerodhaWrapper.getScriptCode(pe2ScriptName);
            String pe3ScriptCode = ZerodhaWrapper.getScriptCode(pe3ScriptName);
            String pe4ScriptCode = ZerodhaWrapper.getScriptCode(pe4ScriptName);
            String pe5ScriptCode = ZerodhaWrapper.getScriptCode(pe5ScriptName);
            
            Map<String,JSONArray> optionsTicksMap = new HashMap<String,JSONArray>();
            optionsTicksMap.put(ce1ScriptName, getScriptData(ce1ScriptCode));
            optionsTicksMap.put(ce2ScriptName, getScriptData(ce2ScriptCode));
            optionsTicksMap.put(ce3ScriptName, getScriptData(ce3ScriptCode));
            optionsTicksMap.put(ce4ScriptName, getScriptData(ce4ScriptCode));
            optionsTicksMap.put(ce5ScriptName, getScriptData(ce5ScriptCode));
            optionsTicksMap.put(pe1ScriptName, getScriptData(pe1ScriptCode));
            optionsTicksMap.put(pe2ScriptName, getScriptData(pe2ScriptCode));
            optionsTicksMap.put(pe3ScriptName, getScriptData(pe3ScriptCode));
            optionsTicksMap.put(pe4ScriptName, getScriptData(pe4ScriptCode));
            optionsTicksMap.put(pe5ScriptName, getScriptData(pe5ScriptCode));
            
        
            
            
            String lastOrderTime = "";
            
            String orderType = "";
            
            for(int i = 1; i < indexTicks.length(); i++){
                String time = indexTicks.getJSONArray(i-1).getString(0);
                double open = indexTicks.getJSONArray(i-1).getDouble(1);
                double close = indexTicks.getJSONArray(i-1).getInt(4);
                
                double ce1Open = optionsTicksMap.get(ce1ScriptName).getJSONArray(i-1).getDouble(1);
                double ce1Close = optionsTicksMap.get(ce1ScriptName).getJSONArray(i-1).getDouble(4);

                double ce2Open = optionsTicksMap.get(ce2ScriptName).getJSONArray(i-1).getDouble(1);
                double ce2Close = optionsTicksMap.get(ce2ScriptName).getJSONArray(i-1).getDouble(4);

                double ce3Open = optionsTicksMap.get(ce3ScriptName).getJSONArray(i-1).getDouble(1);
                double ce3Close = optionsTicksMap.get(ce3ScriptName).getJSONArray(i-1).getDouble(4);

                double ce4Open = optionsTicksMap.get(ce4ScriptName).getJSONArray(i-1).getDouble(1);
                double ce4Close = optionsTicksMap.get(ce4ScriptName).getJSONArray(i-1).getDouble(4);

                double ce5Open = optionsTicksMap.get(ce5ScriptName).getJSONArray(i-1).getDouble(1);
                double ce5Close = optionsTicksMap.get(ce5ScriptName).getJSONArray(i-1).getDouble(4);
                double ce5CurrentOpen = optionsTicksMap.get(ce5ScriptName).getJSONArray(i).getDouble(1);

                double pe1Open = optionsTicksMap.get(pe1ScriptName).getJSONArray(i-1).getDouble(1);
                double pe1Close = optionsTicksMap.get(pe1ScriptName).getJSONArray(i-1).getDouble(4);

                double pe2Open = optionsTicksMap.get(pe2ScriptName).getJSONArray(i-1).getDouble(1);
                double pe2Close = optionsTicksMap.get(pe2ScriptName).getJSONArray(i-1).getDouble(4);

                double pe3Open = optionsTicksMap.get(pe3ScriptName).getJSONArray(i-1).getDouble(1);
                double pe3Close = optionsTicksMap.get(pe3ScriptName).getJSONArray(i-1).getDouble(4);

                double pe4Open = optionsTicksMap.get(pe4ScriptName).getJSONArray(i-1).getDouble(1);
                double pe4Close = optionsTicksMap.get(pe4ScriptName).getJSONArray(i-1).getDouble(4);

                double pe5Open = optionsTicksMap.get(pe5ScriptName).getJSONArray(i-1).getDouble(1);
                double pe5Close = optionsTicksMap.get(pe5ScriptName).getJSONArray(i-1).getDouble(4);
                double pe5CurrentOpen = optionsTicksMap.get(pe5ScriptName).getJSONArray(i).getDouble(1);

                double indexCandleMove = close - open;
                double optionDeltaMomentum = indexCandleMove * optionDelta;
                
                double ce1 = (ce1Close - ce1Open);
                double ce2 = (ce2Close - ce2Open);
                double ce3 = (ce3Close - ce3Open);
                double ce4 = (ce4Close - ce4Open);
                double ce5 = (ce5Close - ce5Open);
                double pe1 = (pe1Close - pe1Open);
                double pe2 = (pe2Close - pe2Open);
                double pe3 = (pe3Close - pe3Open);
                double pe4 = (pe4Close - pe4Open);
                double pe5 = (pe5Close - pe5Open);

                indexMomentumTotal = indexMomentumTotal + optionDeltaMomentum;
                IndexToOptionMomentumCorrelation.indexMomentumTotal = indexMomentumTotal;

                ce1Total = ce1Total + ce1;
                ce2Total = ce2Total + ce2;
                ce3Total = ce3Total + ce3;
                ce4Total = ce4Total + ce4;
                ce5Total = ce5Total + ce5;
                pe1Total = pe1Total + pe1;
                pe2Total = pe2Total + pe2;
                pe3Total = pe3Total + pe3;
                pe4Total = pe4Total + pe4;
                pe5Total = pe5Total + pe5;

                if((ce1Total + ce2Total + ce3Total + ce4Total + ce5Total) > (pe1Total + pe2Total + pe3Total + pe4Total + pe5Total) && orderType != "BUY") {
                    orderType = "BUY";
                    lastOrderTime = time;
                    if(Positions.noOfPositions() == 0) {
                        if(placeLiveOrders) {
                            if(!ZerodhaWrapper.openOrderPresent("sell", ce5ScriptName) && !ZerodhaWrapper.openOrderPresent("sell", pe5ScriptName)) {
                                ZerodhaWrapper.placeOrder("SELL", qty, ce5ScriptName);
                                ZerodhaWrapper.placeOrder("SELL", qty*2, pe5ScriptName);
                            }
                        }

                        Positions.placeOrder(time,ce5ScriptName, "sell", qty, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty*2, pe5CurrentOpen, 0.0);
                        if(printEachOrder) {
                            Positions.printPositions();
                        }
                        totalOrdersBacktest++;
                    }
                    if(  Positions.positionExists(ce5ScriptName, "sell", qty*2)  &&  Positions.positionExists(pe5ScriptName, "sell", qty) )  {
                        if(placeLiveOrders) {
                            if(ZerodhaWrapper.openOrderPresent("sell", ce5ScriptName) && ZerodhaWrapper.openOrderPresent("sell", pe5ScriptName)) {
                                ZerodhaWrapper.placeOrder("BUY", qty*2, ce5ScriptName);
                                ZerodhaWrapper.placeOrder("BUY", qty, pe5ScriptName);
                                ZerodhaWrapper.placeOrder("SELL", qty, ce5ScriptName);
                                ZerodhaWrapper.placeOrder("SELL", qty*2, pe5ScriptName);
                            }
                        }
                        Positions.placeOrder(time,ce5ScriptName, "buy", qty*2, 0.0, ce5CurrentOpen);
                        Positions.placeOrder(time,pe5ScriptName, "buy", qty, 0.0, pe5CurrentOpen);

                        Positions.placeOrder(time,ce5ScriptName, "sell", qty, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty*2, pe5CurrentOpen, 0.0);
                        if(printEachOrder) {
                            Positions.printPositions();
                        }
                        totalOrdersBacktest++;
                    }
                 }else if( (ce1Total + ce2Total + ce3Total+ ce4Total + ce5Total) < (pe1Total + pe2Total + pe3Total  + pe4Total + pe5Total) && orderType != "SELL" ){
                    orderType = "SELL";
                    lastOrderTime = time;
                    if(Positions.noOfPositions() == 0) {
                        if(placeLiveOrders) {
                            if(!ZerodhaWrapper.openOrderPresent("sell", ce5ScriptName) && !ZerodhaWrapper.openOrderPresent("sell", pe5ScriptName)) {
                                ZerodhaWrapper.placeOrder("SELL", qty*2, ce5ScriptName);
                                ZerodhaWrapper.placeOrder("SELL", qty, pe5ScriptName);
                            }
                        }   

                        Positions.placeOrder(time,ce5ScriptName, "sell", qty*2, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty, pe5CurrentOpen, 0.0);
                        if(printEachOrder) {
                            Positions.printPositions();
                        }
                        totalOrdersBacktest++;
                    }
                    if(  Positions.positionExists(ce5ScriptName, "sell", qty)  &&  Positions.positionExists(pe5ScriptName, "sell", qty*2) )  {
                        if(placeLiveOrders) {
                            if(ZerodhaWrapper.openOrderPresent("sell", ce5ScriptName) && ZerodhaWrapper.openOrderPresent("sell", pe5ScriptName)) {
                                ZerodhaWrapper.placeOrder("BUY", qty, ce5ScriptName);
                                ZerodhaWrapper.placeOrder("BUY", qty*2, pe5ScriptName);
                                ZerodhaWrapper.placeOrder("SELL", qty*2, ce5ScriptName);
                                ZerodhaWrapper.placeOrder("SELL", qty, pe5ScriptName);
                            }
                        }

                        Positions.placeOrder(time,ce5ScriptName, "buy", qty, 0.0, ce5CurrentOpen);
                        Positions.placeOrder(time,pe5ScriptName, "buy", qty*2, 0.0, pe5CurrentOpen);

                        Positions.placeOrder(time,ce5ScriptName, "sell", qty*2, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty, pe5CurrentOpen, 0.0);
                        if(printEachOrder) {
                            Positions.printPositions();
                        }
                        totalOrdersBacktest++;
                    }
                }
                if(executeExitOrder) {
                    if((indexTicks.length()/candleSize)-1 == i) {
                        if(orderType.equalsIgnoreCase("BUY")) {
                            Positions.placeOrder(time,ce5ScriptName, "buy", qty, 0.0, ce5CurrentOpen);
                            Positions.placeOrder(time,pe5ScriptName, "buy", qty*2, 0.0, pe5CurrentOpen);
                        }else if(orderType.equalsIgnoreCase("SELL")) {
                            Positions.placeOrder(time,ce5ScriptName, "buy", qty*2, 0.0, ce5CurrentOpen);
                            Positions.placeOrder(time,pe5ScriptName, "buy", qty, 0.0, pe5CurrentOpen);
                        }
                        if(printEachOrder) {
                            Positions.printPositions();
                        }
                        totalOrdersBacktest++;
                    }
                }
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    
    }

    public static JSONArray getScriptData(String scriptCode) throws Exception{
        return ZerodhaWrapper.fetchScriptDayCandles(scriptCode, startDate, endDate, candleSize);
    }

    public static void printPositionInTableFormat() {
        JSONObject position = Positions.getPositions();
        Iterator<String> keys = position.keys();
        System.out.println();
        while(keys.hasNext()){
            String key = keys.next();
            if(indexMomentumTotalLog){
                System.out.println(key + " \t | " + position.getJSONObject(key).getString("time")  + "\t | \t"+ position.getJSONObject(key).getString("entryOrderType") + " \t | \t" + position.getJSONObject(key).getInt("quantity") + " \t | \t " + Math.round(position.getJSONObject(key).getDouble("pnl")) + " \t | \t " + Math.round(indexMomentumTotal ));
            }else {
                System.out.println(key + " \t | " + position.getJSONObject(key).getString("time")  + "\t | \t"+ position.getJSONObject(key).getString("entryOrderType") + " \t | \t" + position.getJSONObject(key).getInt("quantity") + " \t | \t " + Math.round(position.getJSONObject(key).getDouble("pnl")) );
            }
        }
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");

    }

    public static double getTotalPnl() {
        double totalPnl = 0;
        JSONObject position = Positions.getPositions();
        Iterator<String> keys = position.keys();
        while(keys.hasNext()){
            String key = keys.next();
            totalPnl = totalPnl + position.getJSONObject(key).getDouble("pnl");            
        }
        return Math.round(totalPnl);
    }

    public static String updateTimeBy(String datetime, int incrementBy) throws Exception{

        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");  
        Date date=formatter1.parse(datetime);  

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, incrementBy);
        date = cal.getTime(); 
        return formatter1.format(date);
            
    }

    public static void testNoOfSP() {
        index = "NIFTY 50";

        String ce1ScriptName = "";
        String ce2ScriptName = "";
        String ce3ScriptName = "";
        String ce4ScriptName = "";
        String ce5ScriptName = "";
        String pe1ScriptName = "";
        String pe2ScriptName = "";
        String pe3ScriptName = "";
        String pe4ScriptName = "";
        String pe5ScriptName = "";

        INDEX_OPEN_SP = 17451;
        if(index.equalsIgnoreCase("NIFTY BANK")) {
            INDEX_OPEN_SP = ((INDEX_OPEN_SP + 50) / 100 ) * 100;
            ce1ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-400)/100*100) + "CE";
            ce2ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-300)/100*100)  +"CE";
            ce3ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-200)/100*100)  +"CE";
            ce4ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP-100)/100*100)  +"CE";
            ce5ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP)/100*100)  +"CE";
            pe1ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+400)/100*100) +"PE";
            pe2ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+300)/100*100) +"PE";
            pe3ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+200)/100*100)  +"PE";
            pe4ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP+100)/100*100)  +"PE";
            pe5ScriptName = "BANKNIFTY" + expiryDate + ((INDEX_OPEN_SP)/100*100)  +"PE";
        }else {
            INDEX_OPEN_SP = ((INDEX_OPEN_SP + 25) / 50 ) * 50;
            ce1ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-200)/50*50) + "CE";
            ce2ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-150)/50*50)  +"CE";
            ce3ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-100)/50*50)  +"CE";
            ce4ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP-50)/50*50)  +"CE";
            ce5ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP)/50*50)  +"CE";
            pe1ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+200)/50*50) +"PE";
            pe2ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+150)/50*50) +"PE";
            pe3ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+100)/50*50)  +"PE";
            pe4ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP+50)/50*50)  +"PE";
            pe5ScriptName = "NIFTY" + expiryDate + ((INDEX_OPEN_SP)/50*50)  +"PE";
        }
        System.out.println(ce1ScriptName + "," + ce2ScriptName + "," + ce3ScriptName + "," + ce4ScriptName + ","+ ce5ScriptName);
        System.out.println(pe1ScriptName + "," + pe2ScriptName + "," + pe3ScriptName + "," + pe4ScriptName + ","+ pe5ScriptName);
        
    }
}
