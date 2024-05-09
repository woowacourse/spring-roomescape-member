package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.controller.BaseControllerTest;
import roomescape.domain.member.Role;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.MemberResponse;

class MemberControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("회원가입을 한다.")
    void signup() {
        SignupRequest request = new SignupRequest("new@gmail.com", "password", "new");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();

        MemberResponse memberResponse = response.as(MemberResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/members/" + memberResponse.id());
            softly.assertThat(memberResponse.id()).isNotNull();
            softly.assertThat(memberResponse.email()).isEqualTo("new@gmail.com");
            softly.assertThat(memberResponse.name()).isEqualTo("new");
            softly.assertThat(memberResponse.role()).isEqualTo(Role.USER);
        });
    }

    void getAllMembers() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .extract();

        List<MemberResponse> memberResponses = response.jsonPath()
                .getList(".", MemberResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(memberResponses).hasSize(1);
            softly.assertThat(memberResponses.get(0))
                    .isSameAs(new MemberResponse(1L, "new@gmail.com", "new", Role.USER));
        });
    }
}
