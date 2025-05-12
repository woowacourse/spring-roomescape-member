package roomescape.integrate.domain;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.repository.MemberRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTimeIntegrateTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.add(new Member("어드민", "test_admin@test.com", "test", Role.ADMIN));
        token = jwtTokenProvider.createTokenByMember(member);
    }

    @Test
    void 시간_추가_테스트() {
        LocalTime afterTime = LocalTime.now().plusHours(1L);
        Map<String, String> timeParam = Map.of(
                "startAt", afterTime.toString()
        );

        long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(timeParam)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        RestAssured.given().log().all()
                .when().get("/times/ " + timeId)
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 시간_삭제_테스트() {
        LocalTime afterTime = LocalTime.now().plusHours(1L);
        Map<String, String> timeParam = Map.of(
                "startAt", afterTime.toString()
        );

        long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(timeParam)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/times/" + timeId)
                .then().log().all()
                .statusCode(204);
    }
}
