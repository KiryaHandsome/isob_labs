package by.bsuir.util;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

    private static final ObjectMapper objectMapper;

    static {
       objectMapper = new ObjectMapper();
       objectMapper.registerModule(new JavaTimeModule());
       objectMapper.enable(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature());
    }

    @SneakyThrows
    public static String toJson(Object data) {
        return objectMapper.writeValueAsString(data);
    }

    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> type) {
        return objectMapper.readValue(json, type);
    }
}