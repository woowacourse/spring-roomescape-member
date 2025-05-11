package roomescape.api;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
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
public class AdminApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    void 예약_전체_조회() {
        Member savedAdmin = memberRepository.save(
                new Member(null, "admin", "email1@domain.com", "password1", Role.ADMIN)
        );
        String token = tokenProvider.createToken(savedAdmin.getId().toString(), savedAdmin.getRole());

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("api/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }


}
