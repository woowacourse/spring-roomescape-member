package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.MemberAuthenticationException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.AccessToken;
import roomescape.member.dto.LoginRequest;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private MemberDao memberDao;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(memberDao, "this-is-test-secret-key-this-is-test-secret-key");
    }

    @DisplayName("토큰 생성 시, 해당 멤버가 없을 경우 예외를 던진다.")
    @Test
    void makeTokenTest_whenMemberNotExist() {
        LoginRequest request = new LoginRequest("not_exist@abc.com", "1234");
        given(memberDao.findMemberByEmail("not_exist@abc.com")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.makeToken(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인 정보가 잘못 되었습니다.");
    }

    @DisplayName("정상적으로 암호화된 토큰을 해독할 수 있다.")
    @Test
    void decodeTest() {
        String accessToken = makeToken("브리", "bri@abc.com");
        AccessToken expected = new AccessToken(1L, "bri@abc.com", "브리");

        AccessToken actual = authService.decode(accessToken);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("잘못된 토큰을 넣었을 경우, 예외를 던진다.")
    @Test
    void decodeTest_whenWrongToken() {
        String wrongToken = "thisis.not.key";

        assertThatThrownBy(() -> authService.decode(wrongToken))
                .isInstanceOf(MemberAuthenticationException.class);
    }

    @DisplayName("해당 토큰의 유저를 찾을 수 있다.")
    @Test
    void findMember() {
        String accessToken = makeToken("브리", "bri@abc.com");
        given(memberDao.findMemberById(1L)).willReturn(Optional.of(new Member(1L, "브리", "bri@abc.com")));
        Member expected = new Member(1L, "브리", "bri@abc.com");

        Member actual = authService.findMember(accessToken);

        assertThat(actual).isEqualTo(expected);
    }

    private String makeToken(String name, String email) {
        LoginRequest request = new LoginRequest(email, "1234");
        given(memberDao.findMemberByEmail(email))
                .willReturn(Optional.of(new Member(1L, name, email)));
        return authService.makeToken(request);
    }

}
