package oibacktester;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.json.JSONArray;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;    

public class IndiaVIXAnalysis {
    static String previousDate = "2023-03-09"; // yyyy-mm-dd - ZERODHA FORMAT
    static String currentDate = "2023-03-09"; // yyyy-mm-dd - ZERODHA FORMAT
    static BaseBarSeries baseBarSeries = new BaseBarSeries();
    static int adxPeriod = 14;
    static int candleSize = 1;
    static int qty = 50;
    static String stockName = "APOLLOHOSP";


    public static void main(String[] args) throws Exception{
        
        ivBacktest();
       
    }

    public static void ivBacktest() throws Exception {

        String indiaVixCode = ZerodhaWrapper.getScriptCode("INDIA VIX");
        JSONArray indiaVixTicks = getScriptData(indiaVixCode);
        String niftyCode = ZerodhaWrapper.getScriptCode("NIFTY 50");
        JSONArray niftyTicks = getScriptData(niftyCode);

        double highAt10 = 0;
        double lowAt10 = 0;

        double vixTotal = 0;
        for(int i = 1; i < niftyTicks.length(); i++) {
            String time = niftyTicks.getJSONArray(i).getString(0);
            double vixOpen = indiaVixTicks.getJSONArray(i-1).getDouble(1);
            double vixHigh = indiaVixTicks.getJSONArray(i-1).getDouble(2);
            double vixLow = indiaVixTicks.getJSONArray(i-1).getDouble(3);
            double vixClose = indiaVixTicks.getJSONArray(i-1).getDouble(4);

            double niftyOpen = niftyTicks.getJSONArray(i-1).getDouble(1);
            double niftyHigh = niftyTicks.getJSONArray(i-1).getDouble(2);
            double niftyLow = niftyTicks.getJSONArray(i-1).getDouble(3);
            double niftyClose = niftyTicks.getJSONArray(i-1).getDouble(4);


            vixTotal = vixTotal + ((vixClose-vixOpen) * 59.269);

            System.out.println(time + ", VIX Value " +  vixTotal + ", NIFTY Value " + (niftyClose - niftyOpen));

        }

    }    
    
    public static JSONArray getScriptData(String scriptCode) throws Exception{
        return ZerodhaWrapper.fetchScriptDayCandles(scriptCode, previousDate, currentDate, candleSize);
    }

}
