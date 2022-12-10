/**

Sure, here's an example of an Apache Pulsar Function written in Java that ingests messages from a Slack channel via the Slack API and sends the data as JSON to a Pulsar topic:

This Pulsar Function listens for incoming messages from a Slack channel, extracts the relevant fields from the message, and then sends the data as a JSON object to a Pulsar topic. The Pulsar Function uses the Gson library to parse the JSON message and build a new JSON object to send to the Pulsar topic.

I hope this helps! Let me know if you have any other questions.

*/
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.gson.Gson;

import java.util.Map;

public class SlackMessageIngestor implements Function<String, Void> {

    private static final Gson GSON = new Gson();

    @Override
    public Void process(String messageJson, Context context) {
        // Parse the message JSON into a Map
        Map<String, Object> message = GSON.fromJson(messageJson, Map.class);

        // Extract the relevant fields from the message
        String channelId = (String) message.get("channel");
        String userId = (String) message.get("user");
        String userName = (String) message.get("username");
        String content = (String) message.get("text");

        // Build a JSON object with the extracted data
        Map<String, Object> slackMessage = new HashMap<>();
        slackMessage.put("channel_id", channelId);
        slackMessage.put("user_id", userId);
        slackMessage.put("user_name", userName);
        slackMessage.put("content", content);

        // Send the message to the "slack-messages" topic on Pulsar
        context.newOutputMessage(
            "persistent://public/default/slack-messages",
            Schema.JSON(Map.class)
        ).value(slackMessage).sendAsync();

        return null;
    }
}
