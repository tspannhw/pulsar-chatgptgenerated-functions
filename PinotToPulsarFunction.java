/**

Here is an example of an Apache Pulsar Function written in Java that reads from Apache Pinot and writes to Apache Pulsar topics as Schema JSON:

In this example, the PinotToPulsarFunction class extends the Function interface from the Apache Pulsar Functions API. The open() method is used to open a connection to the Pinot broker, the process() method is used to execute the Pinot query, convert the result set to JSON, and write the JSON to a Pulsar topic, and the close() method is used to close the connection to the Pinot broker.

To use this function, you would first need to build it into a JAR file and then deploy it to your Pulsar cluster using the pulsar-admin command-line tool or the Pulsar Functions dashboard.

*/

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pinot.client.Connection;
import org.apache.pinot.client.ConnectionFactory;
import org.apache.pinot.client.ResultSet;
import org.apache.pinot.common.utils.DataSchema;
import org.apache.pinot.common.utils.Schema;
import org.apache.pinot.common.utils.StringUtils;

public class PinotToPulsarFunction implements Function<String, Void> {

  private Connection pinotConnection;
  private String pinotBrokerUrl;
  private String pinotQuery;

  public PinotToPulsarFunction() {
    // Set the Pinot broker URL and query in the constructor
    this.pinotBrokerUrl = "http://localhost:9000";
    this.pinotQuery = "SELECT * FROM my_table";
  }

  @Override
  public void open(Context context) {
    // Open a connection to the Pinot broker
    this.pinotConnection = ConnectionFactory.fromUrl(pinotBrokerUrl);
  }

  @Override
  public Void process(String input, Context context) {
    // Execute the Pinot query and get the result set
    ResultSet resultSet = pinotConnection.execute(pinotQuery);

    // Get the schema for the result set
    DataSchema dataSchema = resultSet.getDataSchema();
    Schema schema = new Schema.SchemaBuilder().setSchema(dataSchema.getSchema()).build();

    // Iterate over the rows in the result set
    while (resultSet.hasNext()) {
      // Get the next row
      ResultSet.Row row = resultSet.next();

      // Convert the row to a JSON string
      String jsonString = StringUtils.encodeAsJsonString(row, schema);

      // Write the JSON string to the Pulsar topic
      context.newOutputMessage("my-pulsar-topic", schema)
        .value(jsonString)
        .send();
    }

    return null;
  }

  @Override
  public void close() {
    // Close the connection to the Pinot broker
    pinotConnection.close();
  }

}
