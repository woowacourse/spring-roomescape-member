package roomescape.E2E;

import io.restassured.RestAssured;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.MemberRoleType;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthE2ETest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("잘못된 토큰일 시, 401을 응답한다")
    void wrongToken() {
        //given
        String token = jwtProvider.generateToken(new JwtRequest(100, "notExist", MemberRoleType.MEMBER, new Date(0)));

        //when //then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }
}
