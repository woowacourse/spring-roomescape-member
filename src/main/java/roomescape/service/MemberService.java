package roomescape.service;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.exception.AuthFailException;
import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(MemberCreateRequest request) {
        Member member = memberDao.readMemberByEmailAndPassword(request)
                .orElseThrow(AuthFailException::new);
        String accessToken = jwtTokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMember(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        Member member = jwtTokenProvider.findMember(token);
        return MemberResponse.from(member);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

}
