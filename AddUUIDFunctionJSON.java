/*
Here is an example of an Apache Pulsar function written in Java that adds a UUID as a key and primary key to an incoming Apache Pulsar topic before publishing it to a new topic with a JSON schema:

This function generates a UUID, adds it as a key and primary key to the incoming topic, and then publishes the updated message to a new topic with a JSON schema.

Note that this is just an example and may need to be modified to fit your specific use case.

*/

import java.util.UUID;

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class AddUUIDFunction implements Function<String, String> {

    @Override
    public String process(String input, Context context) {
        // Generate a UUID
        String uuid = UUID.randomUUID().toString();

        // Add the UUID as a key and primary key to the incoming topic
        String output = String.format("{\"uuid\": \"%s\", \"message\": %s}", uuid, input);

        // Publish the updated message to the new topic with a JSON schema
        context.newOutputMessage("new-topic", Schema.JSON(String.class))
              .value(output)
              .key(uuid)
              .send();

        return output;
    }
}
