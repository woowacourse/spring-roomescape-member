package roomescape.member.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("모든 사용자 조회 성공 테스트 - 사용자 총 3명")
    @Test
    @Sql(scripts = {"classpath:insert-members.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllMembers() {
        //when&then
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .assertThat().statusCode(200).body("size()", is(3));
    }

}
