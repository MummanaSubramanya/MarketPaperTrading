package oibacktester;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZerodhaWrapper {
  
  static String encToken = "<TOKEN>";
  public static void main(String[] args) throws Exception {
    System.out.println(getScriptCode("NIFTY 50"));
    
    int indexStartPrice = 38300;
    int indexEndPrice = 44300;
    String index = "BANKNIFTY";
    String expiry = "23309";
    
    
    while(indexStartPrice <= indexEndPrice) {
      String scriptNameCE = index + expiry + indexStartPrice + "CE";
      String scriptNamePE = index + expiry + indexStartPrice + "PE";
      downloadOptionChain(scriptNameCE,"2023-02-01","2023-03-09");
      downloadOptionChain(scriptNamePE,"2023-02-01","2023-03-09");

      indexStartPrice = indexStartPrice + 100;
    }
    
  }

  public static JSONArray fetchScriptDayCandles(
    String scriptCode,
    String currentDate,
    int candleSize
  ) throws Exception {
    String url =
      "https://kite.zerodha.com/oms/instruments/historical/" +
      scriptCode +
      "/" +
      candleSize +
      "minute?user_id=USERID&oi=1&from=" +
      currentDate +
      "&to=" +
      currentDate +
      "";
    if (candleSize == 1) {
      url =
        "https://kite.zerodha.com/oms/instruments/historical/" +
        scriptCode +
        "/minute?user_id=USERID&oi=1&from=" +
        currentDate +
        "&to=" +
        currentDate +
        "";
    }
    Unirest.setTimeouts(0, 0);
    HttpResponse<String> response = Unirest
      .get(url)
      .header(
        "authorization",
        " enctoken "+ encToken
      )
      .asString();
    JSONArray candles = new JSONObject(response.getBody())
      .getJSONObject("data")
      .getJSONArray("candles");
    return candles;
  }

  public static JSONArray fetchScriptDayCandles(
    String scriptCode,
    String previousDate,
    String currentDate,
    int candleSize
  ) throws Exception {
    String url =
      "https://kite.zerodha.com/oms/instruments/historical/" +
      scriptCode +
      "/" +
      candleSize +
      "minute?user_id=USERID&oi=1&from=" +
      previousDate +
      "&to=" +
      currentDate +
      "";
    if (candleSize == 1) {
      url =
        "https://kite.zerodha.com/oms/instruments/historical/" +
        scriptCode +
        "/minute?user_id=USERID&oi=1&from=" +
        previousDate +
        "&to=" +
        currentDate +
        "";
    }
    Unirest.setTimeouts(0, 0);
    HttpResponse<String> response = Unirest
      .get(url)
      .header(
        "authorization",
        " enctoken "+encToken
      )
      .asString();
    JSONArray candles = new JSONObject(response.getBody())
      .getJSONObject("data")
      .getJSONArray("candles");
    return candles;
  }

  public static String getScriptCode(String scriptName) throws Exception {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    LocalDateTime now = LocalDateTime.now();
    final String todaysDate = dtf.format(now);
    try {
      File checkFileExist = new File("./instruments" + todaysDate + ".csv");
      if (!checkFileExist.exists()) {
        System.out.println("Fetching instrument list for today");
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest
          .get("https://api.kite.trade/instruments")
          .asString();
        FileWriter fileWriter = new FileWriter(
          "instruments" + todaysDate + ".csv"
        );
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(response.getBody());
        printWriter.close();
      } else {
        // System.out.println("Not fetching instruments as todays file found");
      }

      File myObj = new File("instruments" + todaysDate + ".csv");
      Scanner myReader = new Scanner(myObj);
      ArrayList<String> rowData = new ArrayList<String>();
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        rowData.add(data);
      }
      myReader.close();

      for(int i = 0; i < rowData.size(); i++){
        String[] eachRowData = rowData.get(i).split(",");
        if(eachRowData[2].equalsIgnoreCase(scriptName)){
            return eachRowData[0];
        }
      }

    } catch (Exception ex) {}
    return null;
  }


  public static String getScriptName(String scriptCode) throws Exception {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    LocalDateTime now = LocalDateTime.now();
    final String todaysDate = dtf.format(now);
    try {
      File checkFileExist = new File("./instruments" + todaysDate + ".csv");
      if (!checkFileExist.exists()) {
        System.out.println("Fetching instrument list for today");
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest
          .get("https://api.kite.trade/instruments")
          .asString();
        FileWriter fileWriter = new FileWriter(
          "instruments" + todaysDate + ".csv"
        );
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(response.getBody());
        printWriter.close();
      } else {
        // System.out.println("Not fetching instruments as todays file found");
      }

      File myObj = new File("instruments" + todaysDate + ".csv");
      Scanner myReader = new Scanner(myObj);
      ArrayList<String> rowData = new ArrayList<String>();
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        rowData.add(data);
      }
      myReader.close();

      for(int i = 0; i < rowData.size(); i++){
        String[] eachRowData = rowData.get(i).split(",");
        if(eachRowData[0].equalsIgnoreCase(scriptCode)){
            return eachRowData[2];
        }
      }

    } catch (Exception ex) {}
    return null;
  }

  public static void placeOrder(String orderType, int qty, String stockName) {
      try {
          HttpResponse<String> response = Unirest.post("https://api.kite.trade/orders/regular")
                  .header("x-kite-version", "3").header("authorization", "enctoken " + encToken)
                  .header("content-type", "application/x-www-form-urlencoded").header("cache-control", "no-cache")
                  .body("" + "exchange=NFO&" + "tradingsymbol=" + stockName + "&" + "transaction_type=" + orderType
                          + "&" + "order_type=MARKET&" + "quantity=" + qty + "&" + "price=0&" + "product=MIS&"
                          + "validity=DAY&" + "disclosed_quantity=0&" + "squareoff=0&" + "stoploss=0&"
                          + "trailing_stoploss=0&" + "variety=regular&" + "user_id=USERID")
                  .asString();

          System.out.println(response.getBody());
      } catch (Exception ex) {
          System.out.println("Issue while placing Order with Type = " + orderType);
          System.out.println(ex.getMessage());
      }
  }

  public static boolean openOrderPresent(String orderType, String stockName) {
      try {
          HttpResponse<String> response = Unirest.get("https://api.kite.trade/portfolio/positions")
                  .header("x-kite-version", "3").header("authorization", "enctoken " + encToken)
                  .header("content-type", "application/x-www-form-urlencoded").header("cache-control", "no-cache")
                  .asString();

          JSONObject positions = new JSONObject(response.getBody());
          JSONArray positionArr = positions.getJSONObject("data").getJSONArray("day");
          String orderTypeFlag = "";

          if (positionArr.length() == 0) {
              return false;
          }
          for (int i = 0; i < positionArr.length(); i++) {
              if ((positionArr.getJSONObject(i).get("tradingsymbol").toString().equalsIgnoreCase(stockName))) {
                  if (Integer.parseInt(positionArr.getJSONObject(i).get("quantity").toString()) > 0) {
                      orderTypeFlag = "buy";
                  } else if (Integer.parseInt(positionArr.getJSONObject(i).get("quantity").toString()) < 0) {
                      orderTypeFlag = "sell";
                  }

                  if (orderType.equalsIgnoreCase(orderTypeFlag)) {
                      System.out.println(orderType + " is Already in <b>OPEN</b> State ");
                      return true;
                  }
              }
          }
      } catch (UnirestException ex) {
          System.out.println("issue while fetching orders");
          System.out.println(ex.getMessage());
      }
      return false;
  }
  
  public static void downloadOptionChain(String scriptName, String currentDate, String endDate) throws Exception {
    String scriptCode = getScriptCode(scriptName);
    System.out.println(scriptCode);
    String url =
      "https://kite.zerodha.com/oms/instruments/historical/" +
      scriptCode +
      "/minute?user_id=USERID&oi=1&from=" +
      currentDate +
      "&to=" +
      endDate +
      "";
    Unirest.setTimeouts(0, 0);
    HttpResponse<String> response = Unirest
      .get(url)
      .header(
        "authorization",
        " enctoken "+ encToken
      )
      .asString();
    JSONObject candles = new JSONObject(response.getBody());
    PrintWriter printWriter = new PrintWriter(scriptName+".json");
    printWriter.print(candles);
    printWriter.close();
  }

}
