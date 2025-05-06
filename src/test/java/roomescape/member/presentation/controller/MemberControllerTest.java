package roomescape.member.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.createMemberByIdAndName;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.response.MemberResponse;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("모든 멤버를 조회할 수 있다.")
    @Test
    void getAllMembers() {
        // given
        Member member1 = createMemberByIdAndName(1L, "멍구");
        Member member2 = createMemberByIdAndName(2L, "아이나");
        JdbcHelper.insertMembers(jdbcTemplate, member1, member2);

        // when & then
        List<MemberResponse> responses =
                RestAssured.given().log().all()
                        .when().get("/members")
                        .then().log().all()
                        .statusCode(200).extract()
                        .jsonPath().getList(".", MemberResponse.class);

        assertThat(responses).hasSize(2);
        assertThat(responses)
                .extracting(MemberResponse::name)
                .containsExactly("멍구", "아이나");
    }
}
