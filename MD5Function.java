/**
To write an Apache Pulsar Function in Java that calculates the MD5 hash of incoming JSON records, you can use the PulsarFunction interface provided by the Pulsar Function API. This interface defines a single method, process, that takes in a PulsarMessage object and returns a PulsarMessage object.

Here is an example implementation of the process method that calculates the MD5 hash of incoming JSON records:

In this example, the process method receives a PulsarMessage object that contains a JSON record in its value field. It parses the JSON record into a Java JsonObject, calculates the MD5 hash of the object using the Hashing utility class from the Guava library, adds the hash as a new field in the JSON object, and returns the modified JSON object as the output of the function.

Note that this example uses the Guava library to calculate the MD5 hash, but other libraries, such as the Apache Commons Codec library, could also be used. The choice of library will depend on your specific requirements and preferences.

*/
import com.google.common.hash.Hashing;
import org.apache.pulsar.functions.api.Record;

public class MD5Function implements PulsarFunction<String, String> {
  @Override
  public String process(PulsarMessage<String> input) {
    // Parse the JSON record into a Java object
    JsonObject json = new JsonParser().parse(input.getValue()).getAsJsonObject();

    // Calculate the MD5 hash of the JSON object
    String hash = Hashing.md5()
      .hashString(json.toString(), StandardCharsets.UTF_8)
      .toString();

    // Add the hash as a new field in the JSON object
    json.addProperty("md5", hash);

    // Return the modified JSON object as the output of the function
    return json.toString();
  }
}

