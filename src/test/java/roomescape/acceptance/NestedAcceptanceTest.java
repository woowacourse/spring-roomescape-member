package roomescape.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

public class NestedAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    private void setUp() {
        RestAssured.port = port;
    }
}
