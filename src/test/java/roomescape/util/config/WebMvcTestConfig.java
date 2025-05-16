package roomescape.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.TokenExtractor;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.repository.fake.MemberFakeRepository;
import roomescape.util.fixture.AuthFixture;

@TestConfiguration
public class WebMvcTestConfig implements WebMvcConfigurer {

    @Bean
    public AuthService authService(
            MemberRepository memberRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        return new AuthService(memberRepository, jwtTokenProvider, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberRepository memberRepository(PasswordEncoder passwordEncoder) {
        MemberFakeRepository memberRepository = new MemberFakeRepository();
        memberRepository.save(
                new Member(1L, "관리자", AuthFixture.ADMIN_EMAIL, passwordEncoder.encode(AuthFixture.ADMIN_PASSWORD),
                        Role.ADMIN));
        memberRepository.save(
                new Member(2L, "사용자", AuthFixture.USER_EMAIL, passwordEncoder.encode(AuthFixture.USER_PASSWORD),
                        Role.USER));

        return memberRepository;
    }

    @Bean
    public TokenExtractor tokenExtractor() {
        return new TokenExtractor();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${security.jwt.token.secret.key}") String secretKey,
            @Value("${security.jwt.token.expiration}") Long expirationInMilliseconds
    ) {
        return new JwtTokenProvider(secretKey, expirationInMilliseconds);
    }

}
