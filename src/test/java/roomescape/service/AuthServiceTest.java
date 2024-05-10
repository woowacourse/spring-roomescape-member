package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.MEMBER_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.createMember;

import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("사용자 정보로 쿠키 생성")
    @Test
    void generateCookie() {
        // given
        createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        Member foundMember = memberDao.find(MEMBER_FIXTURE);
        // when
        Cookie cookie = authService.generateCookie(foundMember);
        // then
        assertAll(
                () -> assertThat(cookie.getName()).isEqualTo("token"),
                () -> assertThat(cookie.getPath()).isEqualTo("/")
        );
    }

    @DisplayName("쿠키를 이용한 사용자 id 조회 테스트")
    @Test
    void findMemberIdByCookie() {
        // given
        createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        Member foundMember = memberDao.find(MEMBER_FIXTURE);
        Cookie cookie = authService.generateCookie(foundMember);
        Cookie[] cookies = new Cookie[]{cookie};
        // when
        Long id = authService.findMemberId(cookies);
        // then
        assertThat(foundMember.getId()).isEqualTo(id);
    }

    @DisplayName("쿠키를 이용한 사용자 권한 조회 테스트")
    @Test
    void findMemberRoleByCookie() {
        // given
        createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        Member foundMember = memberDao.find(MEMBER_FIXTURE);
        Cookie cookie = authService.generateCookie(foundMember);
        Cookie[] cookies = new Cookie[]{cookie};
        // when
        Long id = authService.findMemberId(cookies);
        // then
        assertThat(foundMember.getRoleName()).isEqualTo(Role.NORMAL.name());
    }
}
