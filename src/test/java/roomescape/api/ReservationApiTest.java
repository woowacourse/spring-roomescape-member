package roomescape.api;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.Role;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.infrastructure.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/data.sql")
public class ReservationApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    void 예약_추가_테스트() {
        Member savedMember = memberRepository.save(
                new Member(null, "name1", "email1@domain.com", "password1", Role.MEMBER)
        );
        String token = tokenProvider.createToken(savedMember.getId().toString(), savedMember.getRole());

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2026-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .cookie("token", token)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201)
                .body("date", equalTo("2026-08-05"))
                .body("memberName", equalTo("name1"));

    }

    @Test
    void 에약_삭제_테스트() {
        RestAssured.given().log().all()
                .when().delete("/api/reservations/3")
                .then().log().all()
                .statusCode(204);
    }
}
