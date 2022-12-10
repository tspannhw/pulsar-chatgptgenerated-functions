/**

Sure, here's an example of an Apache Pulsar Function written in Java that ingests event messages from a Github repository via the Github Event API and sends the data as JSON to a Pulsar topic:

This Pulsar Function listens for incoming events from a Github repository, extracts the relevant fields from the event, and then sends the data as a JSON object to a Pulsar topic. The Pulsar Function uses the Gson library to parse the JSON event and build a new JSON object to send to the Pulsar topic.

I hope this helps! Let me know if you have any other questions.

*/

import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import com.google.gson.Gson;

import java.util.Map;

public class GithubEventIngestor implements Function<String, Void> {

    private static final Gson GSON = new Gson();

    @Override
    public Void process(String eventJson, Context context) {
        // Parse the event JSON into a Map
        Map<String, Object> event = GSON.fromJson(eventJson, Map.class);

        // Extract the relevant fields from the event
        String repository = (String) event.get("repository");
        String action = (String) event.get("action");
        String createdAt = (String) event.get("created_at");

        // Build a JSON object with the extracted data
        Map<String, Object> githubEvent = new HashMap<>();
        githubEvent.put("repository", repository);
        githubEvent.put("action", action);
        githubEvent.put("created_at", createdAt);

        // Send the event to the "github-events" topic on Pulsar
        context.newOutputMessage(
            "persistent://public/default/github-events",
            Schema.JSON(Map.class)
        ).value(githubEvent).sendAsync();

        return null;
    }
}
