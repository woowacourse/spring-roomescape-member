package roomescape;


import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WelcomeTest {

    private final int port;

    public WelcomeTest(
            @LocalServerPort final int port
    ){
        this.port = port;
    }

    @DisplayName("/로 요청이 들어오면 웰컴 페이지를 응답한다.")
    @Test
    void welcome() {
        RestAssured.given().port(port).log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }
}
