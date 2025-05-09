package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.exception.MemberException;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(final LoginRequest request) {
        Member member = findMemberBy(request.email());
        member.validatePassword(request.password());
        return jwtTokenProvider.createToken(member);
    }

    private Member findMemberBy(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberException("해당 이메일의 회원이 존재하지 않습니다."));
    }

}
