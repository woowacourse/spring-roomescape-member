package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.auth.LoginCheckResponse;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.LoginResponse;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.NotFoundException;

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

    public LoginCheckResponse getMemberIdFromToken(final String accessToken) {
        Long memberId = jwtHandler.getMemberIdFromToken(accessToken);
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.MEMBER_NOT_FOUND,
                        String.format("회원(Member) 정보가 존재하지 않습니다. [memberId: %d]", memberId)));

        return new LoginCheckResponse(member.getName());
    }
}
