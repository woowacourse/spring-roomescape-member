package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.AuthorizationException;
import roomescape.common.exception.InvalidIdException;
import roomescape.member.dao.MemberDao;
import roomescape.member.dao.MemberDaoImpl;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignupRequest;
import roomescape.member.dto.MemberTokenResponse;
import roomescape.member.login.authorization.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberDao = mock(MemberDaoImpl.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        memberService = new MemberService(jwtTokenProvider, memberDao);
    }

    @DisplayName("회원 목록을 조회하는 기능을 구현한다")
    @Test
    void findAll() {
        Member member = new Member(1L, "admin", "admin@email.com", "password123");
        when(memberDao.findAll()).thenReturn(List.of(member));

        List<MemberResponse> members = memberService.findAll();

        assertThat(members).hasSize(1);
        verify(memberDao, times(1)).findAll();
    }

    @DisplayName("회원을 아이디로 조회하는 기능을 구현한다")
    @Test
    void findById() {
        Member member = new Member(1L, "admin", "admin@email.com", "password123");
        when(memberDao.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse foundMember = memberService.findById(1L);

        assertThat(foundMember.id()).isEqualTo(1L);
        assertThat(foundMember.name()).isEqualTo("admin");
        verify(memberDao, times(1)).findById(1L);
    }

    @DisplayName("회원을 이메일로 조회하는 기능을 구현한다")
    @Test
    void findByEmail() {
        Member member = new Member(1L, "admin", "admin@email.com", "password123");
        when(memberDao.findByEmail("admin@email.com")).thenReturn(Optional.of(member));

        MemberResponse foundMember = memberService.findByEmail("admin@email.com");

        assertThat(foundMember.email()).isEqualTo("admin@email.com");
        verify(memberDao, times(1)).findByEmail("admin@email.com");
    }

    @DisplayName("회원을 토큰으로 조회하는 기능을 구현한다")
    @Test
    void findByToken() {
        Member member = new Member(1L, "admin", "admin@email.com", "password123");
        when(jwtTokenProvider.getPayloadEmail("valid-token")).thenReturn("admin@email.com");
        when(memberDao.findByEmail("admin@email.com")).thenReturn(Optional.of(member));

        MemberResponse foundMember = memberService.findByToken("valid-token");

        assertThat(foundMember.id()).isEqualTo(1L);
        verify(jwtTokenProvider, times(1)).getPayloadEmail("valid-token");
        verify(memberDao, times(1)).findByEmail("admin@email.com");
    }

    @DisplayName("로그인 토큰을 생성하는 기능을 구현한다")
    @Test
    void createToken() {
        Member member = new Member(1L, "admin", "admin@email.com", "password123");
        when(memberDao.findByEmail("admin@email.com")).thenReturn(Optional.of(member));
        when(memberDao.isPasswordMatch("admin@email.com", "password123")).thenReturn(true);
        when(memberDao.isAdmin("admin@email.com", "password123")).thenReturn(true);
        when(jwtTokenProvider.createToken("admin@email.com", "admin")).thenReturn("access-token");

        MemberLoginRequest loginRequest = new MemberLoginRequest("admin@email.com", "password123");
        MemberTokenResponse tokenResponse = memberService.createToken(loginRequest);

        assertThat(tokenResponse.accessToken()).isEqualTo("access-token");
        verify(memberDao, times(1)).findByEmail("admin@email.com");
        verify(memberDao, times(1)).isPasswordMatch("admin@email.com", "password123");
        verify(memberDao, times(1)).isAdmin("admin@email.com", "password123");
        verify(jwtTokenProvider, times(1)).createToken("admin@email.com", "admin");
    }

    @DisplayName("로그인 시 비밀번호가 일치하지 않는 경우 예외를 발생시킨다")
    @Test
    void exception_invalid_password() {
        Member member = new Member(1L, "admin", "wooteco@gmail.com", "1234A");
        when(memberDao.findByEmail("wooteco@gmail.com")).thenReturn(Optional.of(member));
        when(memberDao.isPasswordMatch("wooteco@gmail.com", "1234A")).thenReturn(false);

        MemberLoginRequest loginRequest = new MemberLoginRequest("wooteco@gmail.com", "1234A");
        assertThatThrownBy(() -> memberService.createToken(loginRequest))
                .isInstanceOf(AuthenticationException.class);

        verify(memberDao, times(1)).findByEmail("wooteco@gmail.com");
        verify(memberDao, times(1)).isPasswordMatch("wooteco@gmail.com", "1234A");
    }

    @DisplayName("회원가입 시 이미 존재하는 이메일인 경우 예외를 발생시킨다")
    @Test
    void exception_duplicate_email() {
        when(memberDao.existsByEmail("admin@email.com")).thenReturn(true);

        MemberSignupRequest signupRequest = new MemberSignupRequest("admin@email.com", "password123", "admin");
        assertThatThrownBy(() -> memberService.add(signupRequest))
                .isInstanceOf(AuthorizationException.class);

        verify(memberDao, times(1)).existsByEmail("admin@email.com");
    }

    @DisplayName("회원가입 시 존재하지 않는 회원 아이디로 조회하는 경우 예외를 발생시킨다")
    @Test
    void exception_invalid_id() {
        when(memberDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById(1L))
                .isInstanceOf(InvalidIdException.class);

        verify(memberDao, times(1)).findById(1L);
    }
}
