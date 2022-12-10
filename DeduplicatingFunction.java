/**

To write an Apache Pulsar Function in Java that deduplicates records, you can use the PulsarFunction interface provided by the Pulsar Function API. This interface defines a single method, process, that takes in a PulsarMessage object and returns a PulsarMessage object.

Here is an example implementation of the process method that deduplicates records:

In this example, the process method receives a PulsarMessage object that contains a record in its value field. It checks if the record has already been seen by checking if it exists in a HashSet of seen records. If the record has already been seen, the method returns null to indicate that it should be discarded. Otherwise, the record is added to the set of seen records and returned as the output of the function.

Note that this example uses a HashSet to store the seen records, but other data structures, such as a HashMap, could also be used. The choice of data structure will depend on the specific requirements of your use case.

*/
public class DeduplicatingFunction implements PulsarFunction<String, String> {
  private Set<String> seenRecords = new HashSet<>();

  @Override
  public String process(PulsarMessage<String> input) {
    String record = input.getValue();

    if (seenRecords.contains(record)) {
      // The record has already been seen, so we return null to indicate that
      // it should be discarded
      return null;
    } else {
      // This is a new record, so we add it to the set of seen records and return
      // it as the output of the function
      seenRecords.add(record);
      return record;
    }
  }
}

