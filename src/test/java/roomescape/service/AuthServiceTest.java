package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;
import roomescape.repository.dao.MemberDao;
import roomescape.service.dto.AuthDto;
import roomescape.service.fakedao.FakeMemberDao;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MemberDao memberDao = new FakeMemberDao(new ArrayList<>(List.of(
                new Member(1, "에버", "treeboss@gmail.com", "treeboss123!"),
                new Member(2, "우테코", "wtc@gmail.com", "wtc123!!")
        )));
        authService = new AuthService(new MemberRepository(memberDao));
    }

    @DisplayName("사용자 정보를 통해 JWT 토큰을 생성한다.")
    @Test
    void login() {
        Member member = new Member(1L, "에버", "treeboss@gmail.com", "treeboss123!");
        AuthDto authDto = new AuthDto(member.getEmail(), member.getPassword());
        String accessToken = authService.createToken(authDto);

        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        Long memberId = Long.valueOf(Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(accessToken)
                .getPayload().getSubject());

        assertThat(memberId).isEqualTo(1L);
    }
}