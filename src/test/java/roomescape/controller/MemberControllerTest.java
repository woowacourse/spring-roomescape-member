package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import roomescape.domain.Role;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.MemberResponse;

class MemberControllerTest extends BaseControllerTest {

    @TestFactory
    @DisplayName("사용자를 생성, 조회, 삭제한다.")
    Stream<DynamicTest> addMemberAndGetAndDelete() {
        return Stream.of(
                DynamicTest.dynamicTest("사용자를 생성한다.", this::addMember),
                DynamicTest.dynamicTest("사용자를 모두 조회한다.", this::getAllMembers),
                DynamicTest.dynamicTest("사용자를 삭제한다.", this::deleteMemberById)
        );
    }

    @TestFactory
    @DisplayName("중복된 email의 사용자를 생성하면 실패한다.")
    Stream<DynamicTest> failWhenDuplicatedEmail() {
        return Stream.of(
                DynamicTest.dynamicTest("사용자를 생성한다.", this::addMember),
                DynamicTest.dynamicTest("이미 존재하는 email의 사용자를 생성한다.", this::addMemberFailWhenDuplicatedEmail)
        );
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 삭제하면 실패한다.")
    void deleteMemberByIdFailWhenNotFoundId() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/members/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).contains("해당 id의 사용자가 존재하지 않습니다.");
        });
    }

    private void addMember() {
        MemberRequest request = new MemberRequest("email@email.com", "password", "사용자");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();

        MemberResponse reservationMemberResponse = response.as(MemberResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/members/1");
            softly.assertThat(reservationMemberResponse)
                    .isEqualTo(new MemberResponse(1L, "email@email.com", "사용자", Role.NORMAL));
        });
    }

    private void addMemberFailWhenDuplicatedEmail() {
        MemberRequest request = new MemberRequest("email@email.com", "password", "사용자");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 email의 사용자가 이미 존재합니다.");
        });
    }

    private void getAllMembers() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .extract();

        List<MemberResponse> reservationMemberResponses = response.jsonPath()
                .getList(".", MemberResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationMemberResponses).hasSize(1);
            softly.assertThat(reservationMemberResponses)
                    .containsExactly(new MemberResponse(1L, "email@email.com", "사용자", Role.NORMAL));
        });
    }

    private void deleteMemberById() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/members/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
