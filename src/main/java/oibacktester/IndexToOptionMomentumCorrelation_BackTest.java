package oibacktester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

public class IndexToOptionMomentumCorrelation {
    

    static String currentDate = "2023-03-03"; // yyyy-mm-dd - ZERODHA FORMAT
    static String expiryDate = "23309";
    static int candleSize = 1;
    static double optionDelta = 0.5;
    static String index = "NIFTY 50";
    static int qty = 50;
    static int INDEX_OPEN_SP = 0;
    static ArrayList<String> indexStrikePrices = new ArrayList<String>();

    public static void main(String[] args) {
        // while(true) {
            System.err.print("[");
            analyse();
            Positions.printPositions(); Positions.clearPositions();
            
            System.out.print(",");
            currentDate = "2023-03-02";
            analyse();
            Positions.printPositions(); Positions.clearPositions();

            System.out.print(",");
            currentDate = "2023-03-01";
            analyse();
            Positions.printPositions(); Positions.clearPositions();

            System.out.print(",");
            currentDate = "2023-02-28";
            analyse();
            Positions.printPositions(); Positions.clearPositions();

            System.out.print(",");
            currentDate = "2023-02-27";
            analyse();
            Positions.printPositions(); Positions.clearPositions();

       
            System.out.println("]");
   
        //     Thread.sleep(2000);
        // }

        // testNoOfSP();
        
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

            Map<Integer,String> ceScriptNameMap = new HashMap();
            ceScriptNameMap.put(1, ce1ScriptName + "," + pe1ScriptName);
            ceScriptNameMap.put(2, ce2ScriptName + "," + pe2ScriptName);
            ceScriptNameMap.put(3, ce3ScriptName + "," + pe3ScriptName);
            ceScriptNameMap.put(4, ce4ScriptName + "," + pe4ScriptName);
            ceScriptNameMap.put(5, ce5ScriptName + "," + pe5ScriptName);
            
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
            JSONArray ce1Ticks = getScriptData(ce1ScriptCode);
            JSONArray ce2Ticks = getScriptData(ce2ScriptCode);
            JSONArray ce3Ticks = getScriptData(ce3ScriptCode);
            JSONArray ce4Ticks = getScriptData(ce4ScriptCode);
            JSONArray ce5Ticks = getScriptData(ce5ScriptCode);
            JSONArray pe1Ticks = getScriptData(pe1ScriptCode);
            JSONArray pe2Ticks = getScriptData(pe2ScriptCode);
            JSONArray pe3Ticks = getScriptData(pe3ScriptCode);
            JSONArray pe4Ticks = getScriptData(pe4ScriptCode);
            JSONArray pe5Ticks = getScriptData(pe5ScriptCode);

            // System.out.println("Fetch of options data complete...");
            

            int noOfBuyOrders = 0;
            int noOfSellOrders = 0;
            String orderType = "";
            String lastOrderTime = "";

            for(int i = 1; i < indexTicks.length(); i++){
                String time = indexTicks.getJSONArray(i-1).getString(0);
                double open = indexTicks.getJSONArray(i-1).getDouble(1);
                double close = indexTicks.getJSONArray(i-1).getInt(4);

                
                double ce1Open = ce1Ticks.getJSONArray(i-1).getDouble(1);
                double ce1Close = ce1Ticks.getJSONArray(i-1).getDouble(4);

                double ce2Open = ce2Ticks.getJSONArray(i-1).getDouble(1);
                double ce2Close = ce2Ticks.getJSONArray(i-1).getDouble(4);

                double ce3Open = ce3Ticks.getJSONArray(i-1).getDouble(1);
                double ce3Close = ce3Ticks.getJSONArray(i-1).getDouble(4);

                double ce4Open = ce4Ticks.getJSONArray(i-1).getDouble(1);
                double ce4Close = ce4Ticks.getJSONArray(i-1).getDouble(4);

                double ce5Open = ce5Ticks.getJSONArray(i-1).getDouble(1);
                double ce5Close = ce5Ticks.getJSONArray(i-1).getDouble(4);
                double ce5CurrentOpen = ce5Ticks.getJSONArray(i).getDouble(1);

                double pe1Open = pe1Ticks.getJSONArray(i-1).getDouble(1);
                double pe1Close = pe1Ticks.getJSONArray(i-1).getDouble(4);

                double pe2Open = pe2Ticks.getJSONArray(i-1).getDouble(1);
                double pe2Close = pe2Ticks.getJSONArray(i-1).getDouble(4);

                double pe3Open = pe3Ticks.getJSONArray(i-1).getDouble(1);
                double pe3Close = pe3Ticks.getJSONArray(i-1).getDouble(4);

                double pe4Open = pe4Ticks.getJSONArray(i-1).getDouble(1);
                double pe4Close = pe4Ticks.getJSONArray(i-1).getDouble(4);

                double pe5Open = pe5Ticks.getJSONArray(i-1).getDouble(1);
                double pe5Close = pe5Ticks.getJSONArray(i-1).getDouble(4);
                double pe5CurrentOpen = pe5Ticks.getJSONArray(i).getDouble(1);

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
                    // System.out.println(ce1ScriptName + "," + ce2ScriptName + "," + ce3ScriptName + "," + ce4ScriptName + ","+ ce5ScriptName);
                    // System.out.println(pe1ScriptName + "," + pe2ScriptName + "," + pe3ScriptName + "," + pe4ScriptName + ","+ pe5ScriptName);
                
                    orderType = "BUY";
                    lastOrderTime = time;
                    if(Positions.noOfPositions() == 0) {
                        Positions.placeOrder(time,ce5ScriptName, "sell", qty, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty*2, pe5CurrentOpen, 0.0);
                    }
                    if(  Positions.positionExists(ce5ScriptName, "sell", qty*2)  &&  Positions.positionExists(pe5ScriptName, "sell", qty) )  {
                        Positions.placeOrder(time,ce5ScriptName, "buy", qty*2, 0.0, ce5CurrentOpen);
                        Positions.placeOrder(time,pe5ScriptName, "buy", qty, 0.0, pe5CurrentOpen);


                        Positions.placeOrder(time,ce5ScriptName, "sell", qty, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty*2, pe5CurrentOpen, 0.0);
                    }
                    noOfBuyOrders++;
                    // Positions.printPositions();
                }else if( (ce1Total + ce2Total + ce3Total+ ce4Total + ce5Total) < (pe1Total + pe2Total + pe3Total  + pe4Total + pe5Total) && orderType != "SELL" ){
                    // System.out.println(ce1ScriptName + "," + ce2ScriptName + "," + ce3ScriptName + "," + ce4ScriptName + ","+ ce5ScriptName);
                    // System.out.println(pe1ScriptName + "," + pe2ScriptName + "," + pe3ScriptName + "," + pe4ScriptName + ","+ pe5ScriptName);
                

                    orderType = "SELL";
                    lastOrderTime = time;
                    if(Positions.noOfPositions() == 0) {
                        Positions.placeOrder(time,ce5ScriptName, "sell", qty*2, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty, pe5CurrentOpen, 0.0);
                    }
                    if(  Positions.positionExists(ce5ScriptName, "sell", qty)  &&  Positions.positionExists(pe5ScriptName, "sell", qty*2) )  {
                        Positions.placeOrder(time,ce5ScriptName, "buy", qty, 0.0, ce5CurrentOpen);
                        Positions.placeOrder(time,pe5ScriptName, "buy", qty*2, 0.0, pe5CurrentOpen);


                        Positions.placeOrder(time,ce5ScriptName, "sell", qty*2, ce5CurrentOpen, 0.0);
                        Positions.placeOrder(time,pe5ScriptName, "sell", qty, pe5CurrentOpen, 0.0);
                    }
                    noOfBuyOrders++;
                    // Positions.printPositions();
                }
                if((375/candleSize)-1 == i) {
                    if(orderType.equalsIgnoreCase("BUY")) {
                        // Positions.placeOrder(closeTimeLastOrder,index, "sell", qty, 0.0, closePriceLastOrder);

                        Positions.placeOrder(time,ce5ScriptName, "buy", qty, 0.0, ce5CurrentOpen);
                        Positions.placeOrder(time,pe5ScriptName, "buy", qty*2, 0.0, pe5CurrentOpen);
                        // Positions.printPositions();

                    }else if(orderType.equalsIgnoreCase("SELL")) {
                        Positions.placeOrder(time,ce5ScriptName, "buy", qty*2, 0.0, ce5CurrentOpen);
                        Positions.placeOrder(time,pe5ScriptName, "buy", qty, 0.0, pe5CurrentOpen);
                        // Positions.placeOrder(closeTimeLastOrder,index, "buy", qty, 0.0, closePriceLastOrder);
                        // Positions.printPositions();
                    }
                }
            }
            // System.out.println(indexMomentumTotal + "," + ce1Total + "," + ce2Total + "," + ce3Total + "," + ce4Total+ "," + ce5Total+ ","+ pe1Total + "," + pe2Total + "," + pe3Total + "," + pe4Total + "," + pe5Total);
            if(orderType.equalsIgnoreCase("BUY")) {
                // System.out.println(lastOrderTime + " BUY ," + " NO OF ORDER = "+ (noOfBuyOrders + noOfSellOrders) + " ("+noOfBuyOrders+" + "+noOfSellOrders+")");
            }else if(orderType.equalsIgnoreCase("SELL")){
                // System.out.println(lastOrderTime + " SELL , " + " NO OF ORDER = "+ (noOfBuyOrders + noOfSellOrders)+ " ("+noOfBuyOrders+" + "+noOfSellOrders+")");
            }
        }catch(Exception ex) {
            ex.printStackTrace();
            // System.out.println("Continue execution....");
        }
    
    }

    public static JSONArray getScriptData(String scriptCode) throws Exception{
        return ZerodhaWrapper.fetchScriptDayCandles(scriptCode, currentDate, currentDate, candleSize);
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
