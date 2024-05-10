package roomescape.acceptance;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberNameResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static roomescape.acceptance.Fixture.secretKey;
import static roomescape.acceptance.PreInsertedData.preInsertedAdmin;
import static roomescape.acceptance.PreInsertedData.preInsertedCustomer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class AuthAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    private void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인, 로그인 후 보이는 이름 확인")
    @TestFactory
    Stream<DynamicTest> login_and_loginCheck_success() {
        LogInRequest customerRequest = new LogInRequest(
                preInsertedCustomer.getEmail(),
                preInsertedCustomer.getPassword()
        );
        LogInRequest adminRequest = new LogInRequest(
                preInsertedAdmin.getEmail(),
                preInsertedAdmin.getPassword()
        );

        return Stream.of(
                dynamicTest("고객이 로그인한다.", () -> {
                    String token = login(customerRequest);

                    Claims claims = parseToken(token);
                    assertAll("토큰에 고객의 식별자와 권한이 포함되어있는지 검증한다.",
                            () -> assertThat(claims.getSubject())
                                    .isEqualTo(preInsertedCustomer.getId().toString()),
                            () -> assertThat(claims.get("role", String.class))
                                    .isEqualTo(preInsertedCustomer.getRole().name())
                    );
                }),

                dynamicTest("고객이 로그인 후 이름을 확인한다.", () -> {
                    String token = login(customerRequest);

                    MemberNameResponse memberNameResponse = checkName(token);

                    assertThat(memberNameResponse.name())
                            .isEqualTo(preInsertedCustomer.getName());
                }),

                dynamicTest("관리자가 로그인한다.", () -> {
                    String token = login(adminRequest);

                    Claims claims = parseToken(token);
                    assertAll("토큰에 관리자의 식별자와 권한이 포함되어있는지 검증한다.",
                            () -> assertThat(claims.getSubject())
                                    .isEqualTo(preInsertedAdmin.getId().toString()),
                            () -> assertThat(claims.get("role", String.class))
                                    .isEqualTo(preInsertedAdmin.getRole().name())
                    );
                }),

                dynamicTest("관리자가 로그인 후 보여지는 이름을 확인한다.", () -> {
                    String token = login(adminRequest);

                    MemberNameResponse memberNameResponse = checkName(token);

                    assertThat(memberNameResponse.name())
                            .isEqualTo(preInsertedAdmin.getName());
                })
        );
    }

    private String login(LogInRequest requestBody) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie("token");
    }

    private MemberNameResponse checkName(String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberNameResponse.class);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
