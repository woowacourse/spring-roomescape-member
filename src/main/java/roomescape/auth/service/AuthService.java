package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.dao.MemberDao;
import roomescape.auth.domain.Member;
import roomescape.auth.dto.MemberLoginRequestDto;
import roomescape.auth.dto.MemberSignUpRequestDto;
import roomescape.configuration.JwtTokenProvider;
import roomescape.exception.RoomEscapeException;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(final MemberLoginRequestDto memberLoginRequestDto) {
        final Member member = memberDao.getByEmailAndPassword(memberLoginRequestDto.email(), memberLoginRequestDto.password());
        final String token = jwtTokenProvider.createToken(member);
        return token;
    }

    public long signUp(final MemberSignUpRequestDto memberSignUpRequestDto) {
        return memberDao.save(memberSignUpRequestDto.toMember());
    }

    public Member loginCheck(final String token) {
        if (jwtTokenProvider.verifyTokenAvailable(token)) {
            throw new RoomEscapeException("기한이 유효하지 않은 토큰입니다.");
        }
        long id = jwtTokenProvider.getPayload(token);
        return memberDao.getById(id);
    }
}
