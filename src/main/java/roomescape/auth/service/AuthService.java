package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.NotFoundException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtHandler jwtHandler;

    public AuthService(final MemberDao memberDao, final JwtHandler jwtHandler) {
        this.memberDao = memberDao;
        this.jwtHandler = jwtHandler;
    }

    public LoginResponse login(final LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new NotFoundException(ErrorType.MEMBER_NOT_FOUND,
                        String.format("회원(Member) 정보가 존재하지 않습니다. [values: %s]", request)));

        String accessToken = jwtHandler.createToken(member.getId());
        return new LoginResponse(member.getId(), accessToken);
    }

    public LoginCheckResponse checkLogin(final Long memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.MEMBER_NOT_FOUND,
                        String.format("회원(Member) 정보가 존재하지 않습니다. [memberId: %d]", memberId)));

        return new LoginCheckResponse(member.getName());
    }
}
