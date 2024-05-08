package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.ADD_MEMBER_SQL;
import static roomescape.TestFixture.MEMBER_FIXTURE;
import static roomescape.TestFixture.MEMBER_NAME_FIXTURE;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberDaoTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM member");
    }

    @Test
    void find() {
        // given
        jdbcTemplate.update(ADD_MEMBER_SQL);
        // when
        Member foundMember = memberDao.find(MEMBER_FIXTURE);
        // then
        assertAll(
                () -> assertThat(foundMember.getNameValue()).isEqualTo(MEMBER_NAME_FIXTURE),
                () -> assertThat(foundMember.getEmail()).isEqualTo(MEMBER_FIXTURE.getEmail()),
                () -> assertThat(foundMember.getPassword()).isEqualTo(MEMBER_FIXTURE.getPassword())
        );
    }
}
