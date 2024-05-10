package roomescape.member.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberControllerTest {

    @Autowired
    private MemberDao memberDao;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("/reservation 으로 GET 요청을 보내면 방탈출 예약 페이지와 200 OK 를 받는다.")
    void getAdminPage() {
        memberDao.insert(new Member("이름1", "test@test.com", "password", Role.MEMBER));
        memberDao.insert(new Member("이름2", "test@test.com", "password", Role.MEMBER));
        memberDao.insert(new Member("이름3", "test@test.com", "password", Role.MEMBER));
        memberDao.insert(new Member("이름4", "test@test.com", "password", Role.MEMBER));

        RestAssured.given().log().all()
                .port(port)
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("data.members.size()", is(4));
    }
}
