import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class IpToLocationFunction implements Function<byte[], String> {
  private static final String GEOIP_DATABASE_PATH = "/path/to/GeoLite2-City.mmdb";

  private DatabaseReader reader;

  @Override
  public void open(Map<String, Object> config, Context context) throws Exception {
    // Open the GeoIP database
    reader = new DatabaseReader.Builder(new File(GEOIP_DATABASE_PATH)).build();
  }

  @Override
  public String process(byte[] input, Context context) throws Exception {
    // Parse the input byte array as a JSON object
    Map<String, Object> json = new ObjectMapper().readValue(input, Map.class);

    // Extract the IP address from the JSON object
    String ip = (String) json.get("ip");

    // Convert the IP address to a location
    InetAddress address = InetAddress.getByName(ip);
    CityResponse response = reader.city(address);
    Location location = response.getLocation();
    Subdivision subdivision = response.getMostSpecificSubdivision();

    // Create a new JSON object with the location data
    Map<String, Object> locationJson = new HashMap<>();
    locationJson.put("latitude", location.getLatitude());
    locationJson.put("longitude", location.getLongitude());
    locationJson.put("city", response.getCity().getName());
    locationJson.put("state", subdivision.getName());
    locationJson.put("country", response.getCountry().getName());

    // Add the location data to the original JSON object
    json.put("location", locationJson);

    // Return the updated JSON object as a string
    return new ObjectMapper().writeValueAsString(json);
  }

  @Override
  public void close() throws Exception {
    // Close the GeoIP database
    reader.close();
  }
}
