package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.ADD_MEMBER_SQL;
import static roomescape.TestFixture.MEMBER_FIXTURE;

import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.response.MemberProfileResponse;

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
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("사용자 정보로 쿠키 생성")
    @Test
    void generateCookie() {
        // given
        jdbcTemplate.update(ADD_MEMBER_SQL);
        Member foundMember = memberDao.find(MEMBER_FIXTURE);
        // when
        Cookie cookie = authService.generateCookie(foundMember);
        // then
        assertAll(
                () -> assertThat(cookie.getName()).isEqualTo("token"),
                () -> assertThat(cookie.getPath()).isEqualTo("/")
        );
    }

    @DisplayName("쿠키를 이용한 사용자 정보 조회 테스트")
    @Test
    void findMemberProfileByCookie() {
        // given
        jdbcTemplate.update(ADD_MEMBER_SQL);
        Member foundMember = memberDao.find(MEMBER_FIXTURE);
        Cookie cookie = authService.generateCookie(foundMember);
        Cookie[] cookies = new Cookie[]{cookie};
        // when
        MemberProfileResponse memberProfile = authService.findMemberProfile(cookies);
        // then
        assertThat(foundMember.getNameValue()).isEqualTo(memberProfile.name());
    }
}
