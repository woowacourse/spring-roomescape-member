package roomescape.controller.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.response.MemberResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@Sql(scripts = "/data.sql")
@Transactional
class MemberControllerIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("/members 요청을 통해 회원 목록을 조회할 수 있다.")
    void should_ReturnAllMembers() {
        //given
        String sql = "SELECT count(*) FROM member";
        int memberCount = jdbcTemplate.queryForObject(sql, Integer.class);

        //when
        List<MemberResponse> responses = RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", MemberResponse.class);

        //then
        assertThat(responses).hasSize(memberCount);
    }
}