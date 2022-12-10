/*
Here is an example of an Apache Pulsar Function written in Java that routes incoming Pulsar topic JSON events with schemas to randomized output topics:

This function extends the IdentityFunction class, which is a pre-defined Pulsar Function class that simply passes the input message to the output topic without modifying it. In this example, the function generates a random integer between 0 and 9 and uses it to route the input message to one of 10 different output topics.

Note that this is just an example, and you can modify it to suit your specific needs. For example, you can specify the output topic names and the probability distribution for routing the messages to each output topic in the function configuration.


*/
import org.apache.pulsar.functions.api.*;
import org.apache.pulsar.functions.api.utils.IdentityFunction;
import java.util.Random;

public class RandomRoutingFunction extends IdentityFunction<String> {

    private Random random;

    // This method is called when the function is initializing
    @Override
    public void setup() {
        random = new Random();
    }

    // This method is called for each message received on the input topic
    @Override
    public String process(String input) {
        // Generate a random integer between 0 and 9
        int randomInt = random.nextInt(10);

        // Route the message to one of 10 different output topics based on the random integer
        switch (randomInt) {
            case 0: return "persistent://my-tenant/my-namespace/output-topic-0";
            case 1: return "persistent://my-tenant/my-namespace/output-topic-1";
            case 2: return "persistent://my-tenant/my-namespace/output-topic-2";
            case 3: return "persistent://my-tenant/my-namespace/output-topic-3";
            case 4: return "persistent://my-tenant/my-namespace/output-topic-4";
            case 5: return "persistent://my-tenant/my-namespace/output-topic-5";
            case 6: return "persistent://my-tenant/my-namespace/output-topic-6";
            case 7: return "persistent://my-tenant/my-namespace/output-topic-7";
            case 8: return "persistent://my-tenant/my-namespace/output-topic-8";
            case 9: return "persistent://my-tenant/my-namespace/output-topic-9";
            default: return "persistent://my-tenant/my-namespace/output-topic-0";
        }
    }

}

