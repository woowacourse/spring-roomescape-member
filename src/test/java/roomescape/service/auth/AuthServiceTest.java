package roomescape.service.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.controller.login.TokenRequest;
import roomescape.controller.login.TokenResponse;
import roomescape.controller.member.MemberResponse;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.repository.H2MemberRepository;
import roomescape.repository.MemberRepository;
import roomescape.service.auth.exception.MemberNotFoundException;
import roomescape.service.auth.exception.TokenNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import({AuthService.class,
        JwtTokenProvider.class,
        H2MemberRepository.class})
@JdbcTest
class AuthServiceTest {

    final List<Member> sampleMembers = List.of(
            new Member(null, "User", "a@b.c", "pw", Role.USER),
            new Member(null, "Admin", "admin@b.c", "pw", Role.ADMIN)
    );

    @Autowired
    AuthService authService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 리스트를 조회한다.")
    void getMembers() {
        // given
        List<MemberResponse> expected = sampleMembers.stream()
                .map(memberRepository::save)
                .map(MemberResponse::from)
                .toList();

        // when
        List<MemberResponse> actual = authService.getMembers();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("이메일을 통해 멤버를 조회한다.")
    void getMemberByEmail() {
        // given
        Member member = sampleMembers.get(0);
        Member expected = memberRepository.save(member);

        // when
        Member actual = authService.getMemberByEmail(member.getEmail());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재 하지 않는 이메일로 멤버를 조회할 경우 예외가 발생한다.")
    void getMemberByEmailNotExist() {
        assertThatThrownBy(() -> authService.getMemberByEmail("no@test.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("토큰을 통해 멤버를 조회 한다.")
    void getMemberByToken() {
        // given
        Member member = sampleMembers.get(0);
        Member expected = memberRepository.save(member);
        TokenRequest request = new TokenRequest(member.getEmail(), member.getPassword());
        String token = authService.createToken(request).token();

        // when
        Member actual = authService.getMemberByToken(token);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰이 공백일 경우 예외가 발생한다.")
    void getMemberByTokenEmpty(String token) {
        assertThatThrownBy(() -> authService.getMemberByToken(token))
                .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    @DisplayName("이메일과 비밀번호로 토큰을 생성을 요청한다.")
    void createToken() {
        // given
        Member member = sampleMembers.get(0);
        memberRepository.save(member);
        TokenRequest request = new TokenRequest(member.getEmail(), member.getPassword());
        TokenResponse expected = new TokenResponse(jwtTokenProvider.createToken(request.email()));

        // when
        TokenResponse actual = authService.createToken(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 멤버의 정보로 토큰을 생성할 경우 예외가 발생한다.")
    void createTokenWithNotExist() {
        // given
        TokenRequest request = new TokenRequest("no@test.com", "pw");

        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(MemberNotFoundException.class);
    }
}