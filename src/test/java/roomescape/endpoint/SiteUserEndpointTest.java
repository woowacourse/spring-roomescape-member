package roomescape.endpoint;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.LogInRequest;
import roomescape.dto.ProfileNameResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.endpoint.PreInsertedData.preInsertedSiteUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class SiteUserEndpointTest {

    @DisplayName("로그인")
    @Test
    void login_success() {
        String email = preInsertedSiteUser.getEmail();
        String password = preInsertedSiteUser.getPassword();
        LogInRequest requestBody = new LogInRequest(email, password);

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie("token");

        assertThat(token).isNotNull();
    }

    @DisplayName("로그인 후 프로필 이름 받아오기")
    @Test
    void loginCheck_success() { //todo: 테스트 최적화 & dinamicTest로 변경하기
        String name = preInsertedSiteUser.getName();
        String email = preInsertedSiteUser.getEmail();
        String password = preInsertedSiteUser.getPassword();
        LogInRequest requestBody = new LogInRequest(email, password);

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie("token");
        ProfileNameResponse response = RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ProfileNameResponse.class);

        assertThat(token).isNotNull();
        assertThat(response.name()).isEqualTo(name);
    }
}
