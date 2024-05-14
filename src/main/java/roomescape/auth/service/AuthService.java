package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.dto.LoginRequestDto;
import roomescape.auth.jwt.JwtTokenProvider;
import roomescape.exception.UnAuthorizationException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(final LoginRequestDto loginRequestDto) {
        final Member member = memberDao.getByEmailAndPassword(loginRequestDto.email(), loginRequestDto.password());
        final String token = jwtTokenProvider.createToken(member);
        return token;
    }

    public Member extractMemberOf(final String token) {
        if (jwtTokenProvider.verifyTokenAvailable(token)) {
            throw new UnAuthorizationException("기한이 유효하지 않은 토큰입니다.");
        }
        long id = jwtTokenProvider.getPayload(token);
        return memberDao.getById(id);
    }
}
