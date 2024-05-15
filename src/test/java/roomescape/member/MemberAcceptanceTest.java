package roomescape.member;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberRequestDto;
import roomescape.member.service.MemberService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
public class MemberAcceptanceTest {

    @LocalServerPort
    private int port;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("사용자를 생성할 수 있다.")
    @Test
    void create() {
        MemberRequestDto memberRequestDto = new MemberRequestDto("hotea@hotea.com", "1234", "hotea", Role.USER);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(memberRequestDto)
                .when().post("/signup")
                .then().statusCode(201);
    }

    @DisplayName("모든 사용자를 검색할 수 있다.")
    @Test
    void findAll() {
        memberService.save(new MemberRequestDto("hotea@hotea.com", "1234", "hotea", Role.USER));
        memberService.save(new MemberRequestDto("zeus@zeus.com", "1234", "zeus", Role.USER));
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get("/members")
                .then().statusCode(200).body("size()", is(2));
    }
}
