/**

Here is an example of a Java function that you can use in Apache Pulsar to read Coinbase data via the REST API:

The function takes in a string as input, which can be used to specify the currency pair (e.g. "BTC-USD") to get the spot price for. It creates a new URL object for the Coinbase API endpoint, opens a connection to the API, and sets the request method and headers. It then reads the response from the API and returns it as a string.

Note: This is just an example and will likely need to be modified to fit your specific use case. You will need to import the necessary classes and provide a valid API key in order for the function to work. You may also want to adjust the code to handle any errors that may occur when making the API request.

*/
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinbaseFunction implements Function<String, String> {

  private static final String API_KEY = "your-api-key";

  @Override
  public String process(String input, Context context) {
    try {
      // Create a new URL object for the Coinbase API endpoint
      URL url = new URL("https://api.coinbase.com/v2/prices/BTC-USD/spot");

      // Open a connection to the Coinbase API
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      // Set the request method and add the API key to the request headers
      con.setRequestMethod("GET");
      con.setRequestProperty("CB-ACCESS-KEY", API_KEY);

      // Read the response from the Coinbase API
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      // Return the response as a string
      return response.toString();
    } catch (Exception e) {
      // Handle any errors
      e.printStackTrace();
      return null;
    }
  }
}
