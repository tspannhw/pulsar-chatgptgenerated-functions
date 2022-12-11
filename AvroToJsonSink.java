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
