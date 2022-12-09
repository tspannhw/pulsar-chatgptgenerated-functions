import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class TranslateFunction implements Function<String, String> {
 @Override
 public String process(String input, Context context) {
  // Set up the Translate API client
  Translate translate = TranslateOptions.getDefaultInstance().getService();

  // Translate the input text from Spanish to English
  Translation translation =
    translate.translate(input, Translate.TranslateOption.sourceLanguage("es"),
      Translate.TranslateOption.targetLanguage("en"));

  return translation.getTranslatedText();
 }
}
