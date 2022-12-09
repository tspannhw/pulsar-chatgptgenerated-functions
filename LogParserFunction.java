/**

log format inputs into JSON:
The function takes in a string as input, which represents a log message in standard format. It splits the input string into its individual fields using the space character as a delimiter. It then creates a new JsonObject and sets the values for the object using the fields from the input string. Finally, it returns the JSON object as a string.

Note: This is just an example and will likely need to be modified to fit your specific use case. You will need to import the necessary classes and adjust the code to parse the fields from the input string based on the specific format of your log messages.

*/
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.gson.JsonObject;

public class LogParserFunction implements Function<String, String> {

  @Override
  public String process(String input, Context context) {
    // Split the input string into its individual fields
    String[] fields = input.split(" ");

    // Create a new JSON object to hold the parsed log data
    JsonObject json = new JsonObject();

    // Set the values for the JSON object using the fields from the input string
    json.addProperty("timestamp", fields[0]);
    json.addProperty("level", fields[1]);
    json.addProperty("message", fields[2]);

    return json.toString();
  }
}
