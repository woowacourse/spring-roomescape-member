package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.common.exception.LoginFailException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(final LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new LoginFailException("이메일 또는 비밀번호가 잘못 되었습니다."));

        String tokenValue = jwtTokenProvider.createToken(member.getEmail());
        return new LoginResponse(tokenValue);
    }
}
