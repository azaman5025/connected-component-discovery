import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;


public class Main {
    private static final String Availability = "availability";
    private static  final String key_48 = "48";
    //private static Iterator<Map.Entry<String, JsonNode>> outerIter;

    public static void main(String[] args) throws IOException {

            try {
                DummyOptimization dummyOptimization = new DummyOptimization();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
}
