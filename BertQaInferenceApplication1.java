/*


Here is an example of an Apache Pulsar application written in Spring Boot 3 that reads Facebook messages, runs a Deep Java Library BertQaInference on the data, and sends the results as JSON with a Schema to Pulsar topics:

*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class BertQaInferenceApplication {

  private static final String FACEBOOK_TOPIC = "persistent://my-pulsar-cluster/my-namespace/facebook-messages";
  private static final String INFERENCE_TOPIC = "persistent://my-pulsar-cluster/my-namespace/inference-results";
  private static final String WORD_VECTORS_FILE = "word-vectors.txt";

  private PulsarClient client;
  private Consumer<String> facebookConsumer;
  private Producer<String> inferenceProducer;
  private ObjectMapper mapper;
  private ParagraphVectors paragraphVectors;

  public static void main(String[] args) {
    SpringApplication.run(BertQaInferenceApplication.class, args);
  }

  public BertQaInferenceApplication() throws PulsarClientException {
    // Create a Pulsar client
    this.client = PulsarClient.builder()
      .serviceUrl("pulsar://localhost:6650")
      .build();

    // Create a consumer for the Facebook messages topic
    this.facebookConsumer = client.newConsumer(Schema.STRING)
      .topic(FACEBOOK_TOPIC)
      .subscriptionName("bert-qa-inference-app")
      .subscribe();

    // Create a producer for the inference results topic
    this.inferenceProducer = client.


