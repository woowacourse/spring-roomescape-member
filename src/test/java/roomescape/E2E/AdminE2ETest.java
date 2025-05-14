package roomescape.E2E;

import io.restassured.RestAssured;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
public class AdminE2ETest {

    @Autowired
    JwtProvider jwtProvider;

    @ParameterizedTest
    @CsvSource({"/members", "/admin", "/admin/reservation", "/admin/time", "/admin/theme"})
    @DisplayName("관리자 권한이 없으면 관리자 리소스에 접근할 수 없다")
    void cannotAccessResource(String path) {

        String token = jwtProvider.generateToken(new JwtRequest(1, "member", MemberRoleType.MEMBER, new Date()));

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get(path)
                .then().log().all()
                .statusCode(403);
    }
}
