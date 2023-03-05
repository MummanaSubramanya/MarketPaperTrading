package oibacktester;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Positions {
    
    static JSONObject positions = new JSONObject();

    public static void main(String[] args){
        // placeOrder("1234", "buy",100, 100.0, 0.0);
        // placeOrder("1234", "sell",100, 0.0, 101.0);
        // placeOrder("1234", "buy",200, 100.0, 0.0);
        // placeOrder("1234", "sell",200, 0.0, 101.0);

        // Positions.placeOrder("BANKNIFTY2330239900CE", "buy", 25, 560.0, 0.0);
        // printPositions();
        // Positions.placeOrder("BANKNIFTY2330239900CE", "sell", 25, 0.0, 577.25);
        // printPositions();
        // Positions.placeOrder("BANKNIFTY2330239900CE", "sell", 25, 577.25, 0.0);
        // printPositions();
        // Positions.placeOrder("BANKNIFTY2330239900CE", "buy", 25, 0.0, 540.0);
        // printPositions();
        // Positions.placeOrder("BANKNIFTY2330239900CE", "buy", 25, 540.0, 0.0);

        printPositions();
    }

    public static boolean placeOrder(String entryTime, String scriptCode, String type, int qty, Double priceAt, Double exitAt){
        JSONObject positionData = new JSONObject();
        if(!positions.has(scriptCode)){
            positionData.put("entryOrderType", type);
            positionData.put("exitOrderType", "");
            positionData.put("status","open");
            positionData.put("entryPriceAt", priceAt);
            positionData.put("quantity",  qty);
            positionData.put("pnl",0);
            positionData.put("exitPriceAt",0);
            positionData.put("time", entryTime);
        }else {
            double existingPnl = positions.getJSONObject(scriptCode).getDouble("pnl");
            double existingEntryPriceAt = positions.getJSONObject(scriptCode).getDouble("entryPriceAt");
            int existingQty = positions.getJSONObject(scriptCode).getInt("quantity");

            if(!positions.getJSONObject(scriptCode).getString("status").equalsIgnoreCase("closed")){
                if(type != positions.getJSONObject(scriptCode).getString("entryOrderType")){
                    positionData.put("entryOrderType",  positions.getJSONObject(scriptCode).getString("entryOrderType"));
                    positionData.put("entryPriceAt", existingEntryPriceAt);
                    positionData.put("quantity",  existingQty - qty);
                    positionData.put("pnl",0);
                    positionData.put("exitOrderType", type);
                    positionData.put("status","closed");
                    positionData.put("exitPriceAt",exitAt);
                    if(type == "buy"){
                        positionData.put("pnl", existingPnl + (existingEntryPriceAt - exitAt) * existingQty);
                    }else {
                        positionData.put("pnl", existingPnl + (exitAt - existingEntryPriceAt) * existingQty);
                    }
                    positionData.put("time", entryTime);
                }else {
                    positionData.put("entryOrderType",  positions.getJSONObject(scriptCode).getString("entryOrderType"));
                    positionData.put("exitOrderType", "");
                    positionData.put("status","open");
                    positionData.put("entryPriceAt", (existingEntryPriceAt * existingQty + priceAt * qty)/(existingQty+qty));
                    positionData.put("quantity",  existingQty + qty);
                    positionData.put("pnl", existingPnl);
                    positionData.put("exitPriceAt",0);
                    positionData.put("time", entryTime);
                }
            }else {
                positionData.put("entryOrderType", type);
                positionData.put("exitOrderType", "");
                positionData.put("status","open");
                positionData.put("entryPriceAt", priceAt);
                positionData.put("quantity",  qty);
                positionData.put("pnl",existingPnl);
                positionData.put("exitPriceAt",0);
                positionData.put("time", entryTime);
            }
            
        }
        // positionData.put("pnl",0);
        positions.put(scriptCode, positionData);

        return true;
    }

    public static double getPositionPnl(){
        double pnl = 0;
        Iterator<String> keys = positions.keys();
        while(keys.hasNext()){
            String key = keys.next();
            JSONObject position = positions.getJSONObject(key);
            if(position.getString("status") == "closed"){
                double entryPriceAt = position.getDouble("entryPriceAt");
                double exitPriceAt = position.getDouble("exitPriceAt");
                String type = position.getString("entryOrderType");
                int exitQuantity = position.getInt("quantity");
                if(type == "buy"){
                    pnl = pnl + ( (exitPriceAt - entryPriceAt) * exitQuantity);
                }else {
                    pnl = pnl + ( (entryPriceAt - exitPriceAt)  * exitQuantity);
                }
            }
        }
        return pnl;
    }

    public static boolean positionExists(String scriptCode, String orderType) {
        if(positions.has(scriptCode) && 
            positions.getJSONObject(scriptCode).getString("status").equalsIgnoreCase("open") && orderType.equalsIgnoreCase(positions.getJSONObject(scriptCode).getString("entryOrderType"))
        ){
            return true;
        }
        return false;
    }

    public static boolean positionExists(String scriptCode, String orderType, int qty) {
        if(positions.has(scriptCode) && 
            positions.getJSONObject(scriptCode).getString("status").equalsIgnoreCase("open") && 
            orderType.equalsIgnoreCase(positions.getJSONObject(scriptCode).getString("entryOrderType")) && 
            qty == positions.getJSONObject(scriptCode).getInt("quantity")
        ){
            return true;
        }
        return false;
    }
    
    public static int noOfPositions(){
        return positions.length();
    }

    public static JSONObject getPositions() {
        return positions;
    }

    public static void printPositions(){
        System.out.println(positions);
    }

    public static void clearPositions() {
        positions = new JSONObject();
    }
}
