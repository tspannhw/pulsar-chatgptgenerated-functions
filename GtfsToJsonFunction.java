/**

Here is an example of a Java function that you can use in Apache Pulsar to convert GTFS traffic data to JSON:



*/

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

public class GtfsToJsonFunction implements Function<byte[], String> {

  @Override
  public String process(byte[] input, Context context) throws InvalidProtocolBufferException {
    // Parse the GTFS traffic data from the input byte array
    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(input);

    // Convert the GTFS data to a JSON string
    String json = JsonFormat.printer().print(feed);

    return json;
  }
}
