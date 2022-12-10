/*
Here is an example of an Apache Pulsar application in Spring Boot 3 that reads Facebook messages and runs a Deep Java Library BertQaInference on the data which it sends as JSON with a Schema to Pulsar topics:

In this example, the Pulsar application creates a Pulsar client, consumer, and producer, and uses them to continuously poll for new messages from the "facebook-messages" topic, run the Deep Java Library BertQaInference on the message, and send the result to the "bert-qa-output" topic.

Note: This code is for illustration purposes only and may not be complete or fully functional. You may need to modify it to meet the specific requirements of your application.

*/

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.pulsar.client.api.ClientBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;

@SpringBootApplication
public class PulsarApplication {

    public static void main(String[] args) throws IOException {
        // Create a Pulsar client
        PulsarClient client = ClientBuilder.create()
            .serviceUrl("pulsar://localhost:6650")
            .build();

        // Create a consumer that subscribes to the "facebook-messages" topic
        Consumer<String> consumer = client.newConsumer(Schema.STRING)
            .topic("facebook-messages")
            .subscribe();

        // Create a producer that sends messages to the "bert-qa-output" topic
        Producer<String> producer = client.newProducer(Schema.STRING)
            .topic("bert-qa-output")
            .create();

        // Continuously poll for new messages from the "facebook-messages" topic
        while (true) {
            // Receive the next message from the "facebook-messages" topic
            Message<String> msg = consumer.receive();

            // Run the Deep Java Library BertQaInference on the message
            BertQaInference bertQaInference = new BertQaInference();
            CompletableFuture<String> resultFuture = bertQaInference.run(msg.getValue());

            // Send the result of the BertQaInference to the "bert-qa-output" topic
            resultFuture.thenAccept(result -> {
                try {
                    producer.send(result);
                } catch (Exception e) {
                    // Handle the error
                }
            });

            // Acknowledge the message
            consumer.acknowledge(msg);
        }

        // Close the consumer, producer, and client
        consumer.close();
        producer.close();
        client.close();

        SpringApplication.run(PulsarApplication.class, args);
    }
}


