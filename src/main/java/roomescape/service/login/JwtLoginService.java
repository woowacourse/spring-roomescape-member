package roomescape.service.login;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.CookieManager;
import roomescape.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.dto.login.TokenRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.repository.member.MemberRepository;

@Service
public class JwtLoginService implements LoginService {

    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public JwtLoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Member findMember(TokenRequest tokenRequest) {
        Member foundMember = memberRepository.findMember(tokenRequest.toMember());
        return foundMember;
    }

    @Override
    public String createToken(TokenRequest tokenRequest) {
        Member member = findMember(tokenRequest);
        String token = jwtTokenProvider.createToken(member);
        return token;
    }

    @Override
    public MemberResponse findMemberByToken(Cookie[] cookies) {
        String token = CookieManager.extractTokenFromCookies(cookies);
        Long id = jwtTokenProvider.findMemberIdByToken(token);
        return MemberResponse.of(memberRepository.findMemberById(id));
    }
}
