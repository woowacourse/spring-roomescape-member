package roomescape.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.restassured.RestAssured;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;
import roomescape.dto.auth.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberDao memberDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("멤버가 존재하면 토큰을 생성한다.")
    @Test
    void createToken() {
        // given
        String email = "email@email.com";
        String password = "password";
        memberDao.insert(new Member(null, "name", email, password, Role.USER));

        // when
        String token = authService.createToken(new LoginRequest(email, password)).accessToken();

        // then
        assertThat(token).isNotBlank();
    }

    @DisplayName("이메일이 일치하지 않으면 토큰 생성시 예외가 발생된다.")
    @Test
    void createTokenWithWrongEmail() {
        // given
        String email = "email@email.com";
        String password = "password";
        memberDao.insert(new Member(null, "name", email, password, Role.USER));

        // when & then
        assertThatThrownBy(() -> authService.createToken(new LoginRequest("wrongEmail@email.com", password)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디, 비밀번호를 확인해주세요.");
    }

    @DisplayName("이메일이 같아도 비밀번호가 다르면 토큰 생성시 예외가 발생된다.")
    @Test
    void createTokenWithWrongPassword() {
        // given
        String email = "email@email.com";
        String password = "password";
        memberDao.insert(new Member(null, "name", email, password, Role.USER));

        // when & then
        assertThatThrownBy(() -> authService.createToken(new LoginRequest(email, "wrongPassword")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디, 비밀번호를 확인해주세요.");
    }

    @DisplayName("토큰을 이용하여 멤버를 조회한다.")
    @Test
    void findMemberByToken() {
        // given
        Member member = new Member(null, "name", "email@email.com", "password", Role.USER);
        memberDao.insert(member);

        // when
        String token = authService.createToken(new LoginRequest(member.getEmail(), member.getPassword())).accessToken();
        MemberInfo foundMember = authService.findMemberByToken(token);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getName()).isEqualTo(member.getName());
    }

    @DisplayName("토큰을 입력받아 해당 멤버가 존재하는지 확인한다.")
    @Test
    void isAllowedMember() {
        // given
        Member member = new Member(null, "user", "user@email.com", "user_password", Role.USER);
        memberDao.insert(member);

        // when
        String token = authService.createToken(new LoginRequest(member.getEmail(), member.getPassword()))
                .accessToken();

        // then
        assertThat(authService.isAllowedMember(token)).isTrue();
    }

    @DisplayName("토큰을 입력받아 해당 멤버가 관리자인지 확인한다.")
    @ParameterizedTest(name = "{index}: role={0}, expected={1}")
    @MethodSource("provideRoleAndExpectedResult")
    void isAdminMember(Role role, boolean expected) {
        // given
        Member member = new Member(null, "name", "email@email.com", "password", role);
        memberDao.insert(member);

        // when
        String token = authService.createToken(new LoginRequest(member.getEmail(), member.getPassword()))
                .accessToken();

        // then
        assertThat(authService.isAdminMember(token)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRoleAndExpectedResult() {
        return Stream.of(
                arguments(Role.USER, false),
                arguments(Role.ADMIN, true)
        );
    }
}
