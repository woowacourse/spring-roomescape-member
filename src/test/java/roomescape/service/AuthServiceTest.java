package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.Member;
import roomescape.model.Role;
import roomescape.repository.MemberRepository;
import roomescape.repository.dao.MemberDao;
import roomescape.service.dto.AuthDto;
import roomescape.service.dto.MemberInfo;
import roomescape.service.fakedao.FakeMemberDao;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MemberDao memberDao = new FakeMemberDao(new ArrayList<>(List.of(
                new Member(1, "에버", "treeboss@gmail.com", "treeboss123!", Role.USER),
                new Member(2, "우테코", "wtc@gmail.com", "wtc123!!", Role.ADMIN)
        )));
        authService = new AuthService(new MemberRepository(memberDao));
    }

    @DisplayName("사용자 정보를 통해 JWT 토큰을 생성한다.")
    @Test
    void should_create_token() {
        Member member = new Member(1L, "에버", "treeboss@gmail.com", "treeboss123!", Role.USER);
        AuthDto authDto = new AuthDto(member.getEmail());

        String accessToken = authService.createToken(authDto);

        MemberInfo memberInfo = authService.checkToken(accessToken);
        assertThat(accessToken).isNotBlank();
        assertThat(memberInfo.getId()).isEqualTo(1L);
    }

    @DisplayName("토큰을 통해 사용자 정보를 조회한다.")
    @Test
    void should_check_login_state() {
        AuthDto authDto = new AuthDto("treeboss@gmail.com");
        String token = authService.createToken(authDto);

        MemberInfo loginMember = authService.checkToken(token);

        assertThat(loginMember.getId()).isEqualTo(1L);
    }
}