package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.LoginResponse;
import roomescape.global.auth.jwt.JwtTokenProvider;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.NotFoundException;

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
                .orElseThrow(() -> new NotFoundException(ErrorType.MEMBER_NOT_FOUND,
                        String.format("회원(Member) 정보가 존재하지 않습니다. [values: %s]", request)));

        String accessToken = jwtTokenProvider.createToken(member.getId());
        return new LoginResponse(member.getId(), accessToken);
    }
}
