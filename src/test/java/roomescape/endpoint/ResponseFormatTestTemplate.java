package roomescape.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class ResponseFormatTestTemplate {

    public static void assertHeaderContainsValue(Response response, String name, String value) {
        assertThat(response.header(name)).contains(value);
    }

    public static <T> void assertBodyFormat(ObjectMapper objectMapper, Response response, Class<T> format) {
        assertThatCode(() -> objectMapper.readValue(response.body().asString(), format))
                .doesNotThrowAnyException();
    }

    public static <T> void assertBodyListFormat(ObjectMapper objectMapper, Response response, Class<T> format) {
        assertThatCode(() -> {
            List<T> list = objectMapper.readValue(response.body().asString(), new TypeReference<>(){});
            objectMapper.readValue(objectMapper.writeValueAsString(list.get(0)), format);
        }).doesNotThrowAnyException();
    }
}
