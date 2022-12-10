import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

/**

This function takes a string input in the same format as the previous example:

Copy code
rules:data
For example:

Copy code
color=red,shape=square:color=red,shape=circle
The rules part of the input string is a list of key-value pairs, separated by commas. The data part of the input string is also a list of key-value pairs, separated by commas. The function creates a new Drools rule engine, parses the rules and





*/
public class DroolsEngineFunction implements Function<String, String> {

  @Override
  public String process(String input, Context context) {
    // Create a new Drools rule engine
    KieServices kieServices = KieServices.Factory.get();
    KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    KieSession kieSession = kieContainer.newKieSession();

    // Parse the input string to extract the rules and data
    String[] parts = input.split(":");
    String rules = parts[0];
    String data = parts[1];

    // Parse the rules string and add each rule to the Drools engine
    String[] ruleParts = rules.split(",");
    for (String rule : ruleParts) {
      String[] rulePair = rule.split("=");
      String key = rulePair[0];
      String value = rulePair[1];

      // Create a new Drools rule object
      Rule ruleObject = new Rule(key, value);

      // Add the rule object to the Drools engine
      kieSession.insert(ruleObject);
    }

    // Parse the data string and add each data point to the Drools engine
    String[] dataParts = data.split(",");
    for (String datum : dataParts) {
      String[] dataPair = datum.split("=");
      String key = dataPair[0];
      String value = dataPair[1];

      // Create a new Drools data object
      Data dataObject = new Data(key, value);

      // Add the data object to the Drools engine
      kieSession.insert(dataObject);
    }

    // Fire the rules in the Drools engine
    kieSession.fireAllRules();

    // Retrieve the result from the Drools engine
    QueryResults queryResults = kieSession.getQueryResults("getResult");
    if (queryResults.size() > 0) {
      // If there is a result, return it
      QueryResult result = queryResults.iterator().next();
      return (String) result.get("result");
    } else {
      // If there is no result, return "N/A"
      return "N/A";
    }
  }
}

