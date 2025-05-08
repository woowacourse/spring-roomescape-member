package roomescape.member.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.presentation.response.MemberResponse;

@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 멤버를_모두_조회한다() {
        // given

        // when & then
        assertThat(memberService.findAll()).hasSize(4);
    }

    @Test
    void id로_테마를_조회한다() {
        // given
        Long id = 1L;

        // when & then
        assertThat(memberService.findById(id)).isEqualTo(
                new MemberResponse(1L, "엠제이")
        );

    }
}
