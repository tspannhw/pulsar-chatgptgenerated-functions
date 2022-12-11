/*
Here is an example of a Pulsar Function written in Java that consumes Pulsar Avro messages and sends them to an HTTP end-point as JSON:
*/

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.io.core.Sink;
import org.apache.pulsar.io.core.SinkContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AvroToJsonSink implements Function<GenericRecord, Void>, Sink<GenericRecord> {
    private Client client;
    private String httpEndpoint;

    public AvroToJsonSink() {
        this.client = ClientBuilder.newClient();
    }

    public AvroToJsonSink(String httpEndpoint) {
        this();
        this.httpEndpoint = httpEndpoint;
    }

    @Override
    public Void process(GenericRecord input, Context context) throws Exception {
        String json = input.toString();
        Response response = client.target(httpEndpoint)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(json));

        // Check the response status code and log an error if the request failed
        if (response.getStatus() != 200) {
            context.getLogger().error("Failed to send JSON to HTTP end-point: {}", response.getStatusInfo().getReasonPhrase());
        }

        return null;
    }

    @Override
    public void open(Map<String, Object> config, SinkContext sinkContext) throws Exception {
        this.httpEndpoint = (String) config.get("httpEndpoint");
    }

    @Override
    public void write(GenericRecord record) throws Exception {
        process(record, null);
    }

    @Override
    public void close() throws Exception {
        client.close();
    }
}

/**

This Pulsar Function consumes Avro messages from a Pulsar topic, converts them to JSON, and sends them to the specified HTTP end-point using an HTTP POST request. It also logs an error if the HTTP request fails.

To use this function, you will need to create a new JAR file containing the function code and all of its dependencies. You can then deploy the function using the Pulsar command-line tools or the Pulsar Functions API.

Here is a sample deploy script for deploying this Pulsar Function:
#!/bin/bash

# Set the Pulsar broker URL and authentication token
export PULSAR_BROKER_URL=<BROKER_URL>
export PULSAR_TOKEN=<AUTH_TOKEN>

# Set the name of the Pulsar Function
export FUNCTION_NAME=<

*/

/*
Here is a sample deploy script for deploying the Pulsar Function that was just created:

*/

#!/bin/bash

# Set the Pulsar broker URL and authentication token
export PULSAR_BROKER_URL=<BROKER_URL>
export PULSAR_TOKEN=<AUTH_TOKEN>

# Set the name of the Pulsar Function
export FUNCTION_NAME=<FUNCTION_NAME>

# Set the JAR file containing the Pulsar Function
export JAR_FILE=<JAR_FILE_PATH>

# Set the class name of the Pulsar Function
export CLASS_NAME=<CLASS_NAME>

# Set the HTTP end-point where the Pulsar Function will send JSON messages
export HTTP_ENDPOINT=<HTTP_ENDPOINT>

# Deploy the Pulsar Function
pulsar-admin functions create \
  --tenant public \
  --namespace default \
  --name $FUNCTION_NAME \
  --inputs persistent://public/default/input-topic \
  --output persistent://public/default/output-topic \
  --jar $JAR_FILE \
  --className $CLASS_NAME \
  --logTopic persistent://public/default/log-topic \
  --resources memory=1G,cpu=1 \
  --sinkConfig '{"httpEndpoint": "'$HTTP_ENDPOINT'"}'

This script assumes that you have already set up the necessary Pulsar broker URL and authentication token, 
as well as the name, JAR file, class name, and HTTP end-point for your Pulsar Function. 
    You will need to replace the placeholder values in the script with the appropriate values for your specific Pulsar Function.

To use this script, save it to a file with a .sh extension, make the file executable (e.g. with chmod +x <SCRIPT_FILE>), 
and run it with ./<SCRIPT_FILE>. This will deploy your Pulsar Function to the specified Pulsar cluster.

I hope this helps! Let me know if you have any other questions.      
      
