package roomescape.infrastructure;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.service.dto.TokenRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JwtTokenProviderTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void testCreateToken() {
        String token = RestAssured.given().log().all()
                .body(new TokenRequest("password", "admin@email.com"))
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");

        assertNotNull(token);
    }
}
