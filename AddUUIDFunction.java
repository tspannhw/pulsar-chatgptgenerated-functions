/*

Here is an example of an Apache Pulsar Function written in Java that adds a UUID as a key and primary key to an incoming Apache Pulsar topic before publishing it to a new topic with a Protobuf schema:

*/
import java.util.UUID;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class AddUUIDFunction implements Function<ProtobufType, ProtobufType> {

  @Override
  public ProtobufType process(ProtobufType input, Context context) {
    // Generate a new UUID for the key
    String uuid = UUID.randomUUID().toString();

    // Set the UUID as the key and primary key of the input message
    input.setKey(uuid);
    input.setPrimaryKey(uuid);

    // i manually set .key(uuid)
    // Publish the modified message to a new topic
    context.newOutputMessage("new-topic", ProtobufSchema.of(ProtobufType.class))
          .key(uuid)
          .value(input)
          .sendAsync();

    return input;
  }
}
