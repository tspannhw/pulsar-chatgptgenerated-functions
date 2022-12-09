/**

Here is an example of a Java function that you can use in Apache Pulsar to calculate quantiles on sensor values that are sent as part of a ProtoBuf record and return the results as a JSON record with an AVRO schema:

The function takes in a byte array as input, which represents a ProtoBuf record containing the sensor values. It parses the record using the SensorDataProto.SensorData class and calculates the quantiles for the sensor values using a helper function calculateQuantiles(). It then creates a new GenericRecord using the output AVRO schema and sets the values for the record. Finally, it returns the GenericRecord as the output of the function.

Note: This is just an example and will likely need to be modified to fit your specific use case. You will need to define the SensorDataProto class and the calculateQuantiles() function based on your data and requirements.

*/

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

public class SensorQuantilesFunction implements Function<byte[], GenericRecord> {

  // Define the AVRO schema for the output record
  private static final String OUTPUT_SCHEMA = "{\"namespace\": \"example.avro\", " +
                                              "\"type\": \"record\"," +
                                              "\"name\": \"SensorData\"," +
                                              "\"fields\": [" +
                                              "{\"name\": \"engine_id\", \"type\": \"string\"}," +
                                              "{\"name\": \"quantiles\", \"type\": {\"type\": \"array\", \"items\": \"double\"}}" +
                                              "]}";

  @Override
  public GenericRecord process(byte[] input, Context context) throws InvalidProtocolBufferException {
    // Parse the ProtoBuf record
    SensorDataProto.SensorData sensorData = SensorDataProto.SensorData.parseFrom(input);

    // Calculate the quantiles for the sensor values
    List<Double> quantiles = calculateQuantiles(sensorData.getValuesList());

    // Create a new GenericRecord using the output schema
    GenericRecord output = new GenericData.Record(new Schema.Parser().parse(OUTPUT_SCHEMA));

    // Set the values for the output record
    output.put("engine_id", sensorData.getEngineId());
    output.put("quantiles", quantiles);

    return output;
  }
}
