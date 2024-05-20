package roomescape.acceptance.admin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import roomescape.acceptance.BaseAcceptanceTest;
import roomescape.domain.Role;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberPreviewResponse;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static roomescape.acceptance.fixture.PreInsertedDataFixture.PRE_INSERTED_ADMIN;

class AuthAcceptanceTest extends BaseAcceptanceTest {

    @DisplayName("관리자가 로그인 후, 로그인 정보를 확인한다.")
    @TestFactory
    Stream<DynamicTest> login_andGetLoginInfo_success() {
        LogInRequest adminRequest = new LogInRequest(
                PRE_INSERTED_ADMIN.getEmail(),
                PRE_INSERTED_ADMIN.getPassword()
        );

        return Stream.of(
                dynamicTest("로그인한다.", () -> {
                            String token = sendLoginRequestWithToken(adminRequest);
                            Claims claims = parseToken(token);

                            assertAll("토큰에 관리자의 식별자와 권한이 포함되어있는지 검증한다.",
                                    () -> assertThat(claims.getSubject())
                                            .isEqualTo(PRE_INSERTED_ADMIN.getId().toString()),
                                    () -> assertThat(claims.get("role", String.class))
                                            .isEqualTo(Role.ADMIN.name())
                            );
                        }
                ),
                dynamicTest("로그인 정보를 확인한다.", () -> {
                            String token = sendLoginRequestWithToken(adminRequest);
                            MemberPreviewResponse response = sendCheckNameRequestWithToken(token);

                            assertThat(response.name())
                                    .isEqualTo(PRE_INSERTED_ADMIN.getName());
                        }
                )
        );
    }

    private String sendLoginRequestWithToken(LogInRequest requestBody) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie("token");
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(tokenFixture.secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private MemberPreviewResponse sendCheckNameRequestWithToken(String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberPreviewResponse.class);
    }

    @DisplayName("관리자가 로그아웃한다.")
    @Test
    void logout_success() {
        RestAssured.given().log().all()
                .cookie("token", tokenFixture.adminToken)
                .when().post("/logout")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .cookie("token", Matchers.emptyString());
    }
}
