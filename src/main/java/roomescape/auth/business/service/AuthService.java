package roomescape.auth.business.service;

import org.springframework.stereotype.Service;
import roomescape.auth.presentation.request.LoginRequest;
import roomescape.auth.presentation.response.LoginCheckResponse;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.global.auth.jwt.dto.TokenDto;
import roomescape.global.exception.impl.NotFoundException;
import roomescape.member.business.model.entity.Member;
import roomescape.member.business.model.repository.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtHandler jwtHandler;

    public AuthService(final MemberDao memberDao, final JwtHandler jwtHandler) {
        this.memberDao = memberDao;
        this.jwtHandler = jwtHandler;
    }

    public TokenDto login(final LoginRequest loginRequest) {
        final Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new NotFoundException("회원 정보가 존재하지 않습니다."));

        return jwtHandler.createToken(member.getId());
    }

    public LoginCheckResponse checkLogin(final Long memberId) {
        final Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 존재하지 않습니다."));

        return new LoginCheckResponse(member.getName());
    }
}
