package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.LoginResponse;
import roomescape.entity.LoginMember;
import roomescape.entity.Role;
import roomescape.exception.AuthenticationException;
import roomescape.repository.MemberDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class MemberServiceTest {

    private MemberDao memberDao;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberDao = Mockito.mock(MemberDao.class);
        memberService = new MemberService(memberDao);
    }

    @Test
    void 로그인에_성공하면_토큰을_반환한다() {
        LoginRequest request = new LoginRequest("admin@email.com", "password");
        LoginMember member = new LoginMember(1L, "어드민", "admin@email.com", Role.ADMIN);

        Mockito.when(memberDao.findByEmailAndPassword(request.email(), request.password()))
            .thenReturn(Optional.of(member));

        LoginResponse response = memberService.login(request);

        assertThat(response.token()).isNotBlank();
        assertThat(response.token()).contains(".");
    }

    @Test
    void 로그인에_실패하면_예외를_던진다() {
        LoginRequest request = new LoginRequest("notfound@email.com", "wrong");

        Mockito.when(memberDao.findByEmailAndPassword(request.email(), request.password()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(request))
            .isInstanceOf(AuthenticationException.class)
            .hasMessage("로그인 정보를 찾을 수 없습니다.");
    }

    @Test
    void ID로_회원정보를_조회한다() {
        LoginMember member = new LoginMember(1L, "어드민", "admin@email.com", Role.ADMIN);
        Mockito.when(memberDao.findById(1L)).thenReturn(Optional.of(member));

        LoginCheckRequest request = memberService.findById(1L);

        assertThat(request.id()).isEqualTo(1L);
        assertThat(request.name()).isEqualTo("어드민");
        assertThat(request.email()).isEqualTo("admin@email.com");
        assertThat(request.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    void ID로_조회시_회원이_없으면_예외를_던진다() {
        Mockito.when(memberDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById(1L))
            .isInstanceOf(AuthenticationException.class)
            .hasMessage("로그인 정보가 일치하지 않습니다.");
    }

    @Test
    void 모든_회원의_이름을_조회한다() {
        List<LoginMember> members = List.of(
            new LoginMember(1L, "어드민", "admin@email.com", Role.ADMIN),
            new LoginMember(2L, "브라운", "brown@email.com", Role.USER)
        );
        Mockito.when(memberDao.findAll()).thenReturn(members);

        List<LoginCheckResponse> result = memberService.findAll();

        assertThat(result).hasSize(2)
            .extracting(LoginCheckResponse::name)
            .containsExactly("어드민", "브라운");
    }
}