package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.MEMBER_FIXTURE;
import static roomescape.TestFixture.MEMBER_NAME_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberDaoTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id");
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @Test
    void find() {
        // given
        simpleJdbcInsert.withTableName("member")
                .execute(MEMBER_PARAMETER_SOURCE);
        // when
        Member foundMember = memberDao.find(MEMBER_FIXTURE).get();
        // then
        assertAll(
                () -> assertThat(foundMember.getNameValue()).isEqualTo(MEMBER_NAME_FIXTURE),
                () -> assertThat(foundMember.getEmail()).isEqualTo(MEMBER_FIXTURE.getEmail()),
                () -> assertThat(foundMember.getPassword()).isEqualTo(MEMBER_FIXTURE.getPassword())
        );
    }

    @Test
    void findById() {
        simpleJdbcInsert.withTableName("member")
                .execute(MEMBER_PARAMETER_SOURCE);
        Long id = memberDao.find(MEMBER_FIXTURE).get().getId();
        // when
        Member foundMember = memberDao.findById(id).get();
        // then
        assertAll(
                () -> assertThat(foundMember.getNameValue()).isEqualTo(MEMBER_NAME_FIXTURE),
                () -> assertThat(foundMember.getEmail()).isEqualTo(MEMBER_FIXTURE.getEmail()),
                () -> assertThat(foundMember.getPassword()).isEqualTo(MEMBER_FIXTURE.getPassword())
        );
    }
}
