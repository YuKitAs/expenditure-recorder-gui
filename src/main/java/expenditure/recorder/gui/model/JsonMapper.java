package expenditure.recorder.gui.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonMapper {
    private static ObjectMapper instance;


    public static synchronized ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper().findAndRegisterModules();
            instance.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            instance.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
            instance.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        }

        return instance;
    }
}
