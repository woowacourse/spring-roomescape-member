package roomescape.util;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class ApiTestSupport {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setRestAssuredPort() {
        RestAssured.reset();
        RestAssured.port = port;
    }
}
