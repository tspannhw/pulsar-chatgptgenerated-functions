/*
To write an Apache Pulsar Function in Java that uses a Bloom filter to process incoming records, you can use the PulsarFunction interface provided by the Pulsar Function API. This interface defines a single method, process, that takes in a PulsarMessage object and returns a PulsarMessage object.

Here is an example implementation of the process method that uses a Bloom filter to process incoming records:

In this example, the process method receives a PulsarMessage object that contains a record in its value field. It first checks if the record might already exist in the Bloom filter by calling the mightContain method. If the record might exist, the contains method is called to check if it actually exists in the filter. If the record exists in the filter, the method returns null to indicate that it should be discarded. Otherwise, the record is added to the filter and returned as the output of the function.

Note that this example uses the BloomFilter class from the Guava library, but other implementations of Bloom filters could also be used. The choice of implementation will depend on your specific requirements and preferences.

*/
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public class BloomFilterFunction implements PulsarFunction<String, String> {
  private BloomFilter<String> filter;

  public BloomFilterFunction() {
    // Create a new Bloom filter with a desired false positive rate of 1%
    // and an estimated number of items to be inserted into the filter
    filter = BloomFilter.create(
      new Funnel<String>() {
        @Override
        public void funnel(String record, PrimitiveSink into) {
          into.putString(record, StandardCharsets.UTF_8);
        }
      },
      1000000,
      0.01
    );
  }

  @Override
  public String process(PulsarMessage<String> input) {
    String record = input.getValue();

    if (filter.mightContain(record)) {
      // The record might already exist in the filter, so we check if it
      // actually exists by calling the `contains` method
      if (filter.contains(record)) {
        // The record does exist in the filter, so we return null to indicate
        // that it should be discarded
        return null;
      }
    }

    // The record does not exist in the filter, so we add it to the filter
    // and return it as the output of the function
    filter.put(record);
    return record;
  }
}
