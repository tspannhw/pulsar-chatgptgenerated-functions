/**
Sure, here's a simple Java application that you can run on a Raspberry Pi to read sensor data, build a JSON object, and send it to a Pulsar topic with a JSON schema. This example assumes that you have a Raspberry Pi with Java installed, as well as a Pulsar instance running and available for use.

*/

import java.util.HashMap;
import java.util.Map;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

public class SensorDataPublisher {

    public static void main(String[] args) {
        // Create a new Pulsar client instance
        PulsarClient client = PulsarClient.builder()
            .serviceUrl("pulsar://localhost:6650")
            .build();

        // Create a JSON schema for the sensor data
        Schema<Map<String, Object>> jsonSchema = Schema.JSON(Map.class);

        try {
            // Read the sensor data
            Map<String, Object> sensorData = readSensorData();

            // Create a new message with the sensor data and JSON schema
            Message<Map<String, Object>> message = Message.builder()
                .value(sensorData)
                .schema(jsonSchema)
                .build();

            // Send the message to the "sensor-data" topic on Pulsar
            client.newProducer(jsonSchema)
                .topic("sensor-data")
                .send(message);

        } catch (PulsarClientException e) {
            // Handle errors sending the message
            e.printStackTrace();
        }
    }

    private static Map<String, Object> readSensorData() {
        // In this example, we're just creating dummy sensor data
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("temperature", 24.5);
        sensorData.put("humidity", 0.65);
        sensorData.put("pressure", 1013.25);
        return sensorData;
    }
}

