package oibacktester;

import java.util.Iterator;

import org.json.JSONObject;

public class Positions {
    
    static JSONObject positions = new JSONObject();

    public static void main(String[] args){
        placeOrder("1234", "buy",100, 100.0, 0.0);
        placeOrder("0987", "sell",50, 100.0, 0.0);

        placeOrder("0987", "buy",50, 0.0, 101.0);
        placeOrder("1234", "buy",100, 99.0, 0.0);
        printPositions();
    }

    public static boolean placeOrder(String scriptCode, String type, int qty, Double priceAt, Double exitAt){
        JSONObject positionData = new JSONObject();
        if(!positions.has(scriptCode)){
            positionData.put("entryOrderType", type);
            positionData.put("exitOrderType", "");
            positionData.put("status","open");
            positionData.put("entryPriceAt", priceAt);
            positionData.put("quantity",  qty);
            positionData.put("pnl",0);
            positionData.put("exitPriceAt",0);
        }else {
            double existingEntryPriceAt = positions.getJSONObject(scriptCode).getDouble("entryPriceAt");
            int existingQty = positions.getJSONObject(scriptCode).getInt("quantity");
            double existingPnl = positions.getJSONObject(scriptCode).getDouble("pnl");

            if(type != positions.getJSONObject(scriptCode).getString("entryOrderType")){
                positionData.put("entryOrderType", positions.getJSONObject(scriptCode).getString("entryOrderType"));
                positionData.put("entryPriceAt", existingEntryPriceAt);
                positionData.put("quantity",  existingQty - qty);
                positionData.put("pnl",0);
                if(existingQty == qty) {
                    positionData.put("exitOrderType", type);
                    positionData.put("status","closed");
                    positionData.put("exitPriceAt",exitAt);
                    if(type == "buy"){
                        positionData.put("pnl", (existingEntryPriceAt - exitAt) * existingQty);
                    }else {
                        positionData.put("pnl", (exitAt - existingEntryPriceAt) * existingQty);
                    }
                }else {
                    positionData.put("exitOrderType", "");
                    positionData.put("status","open");
                    positionData.put("exitPriceAt",0);
                    if(type == "buy"){
                        positionData.put("pnl", existingPnl + (existingEntryPriceAt - exitAt) * qty);
                    }else {
                        positionData.put("pnl", existingPnl + (exitAt - existingEntryPriceAt) * qty);
                    }
                }
            }else {
                positionData.put("entryOrderType", positions.getJSONObject(scriptCode).getString("entryOrderType"));
                positionData.put("exitOrderType", "");
                positionData.put("status","open");
                positionData.put("entryPriceAt", (existingEntryPriceAt * existingQty + priceAt * qty)/(existingQty+qty));
                positionData.put("quantity",  existingQty + qty);
                positionData.put("pnl",existingPnl);
                positionData.put("exitPriceAt",0);
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

    public static int noOfPositions(){
        return positions.length();
    }

    public static void printPositions(){
        System.out.println(positions);
    }

}

