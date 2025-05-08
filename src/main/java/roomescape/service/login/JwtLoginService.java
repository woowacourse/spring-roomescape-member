package roomescape.service.login;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.CookieManager;
import roomescape.JwtTokenProvider;
import roomescape.dto.login.TokenRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.entity.MemberEntity;
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
    public MemberEntity findMember(TokenRequest tokenRequest) {
        return memberRepository.findMember(tokenRequest.toMemberEntity());
    }

    @Override
    public String createToken(TokenRequest tokenRequest) {
        MemberEntity member = findMember(tokenRequest);
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
