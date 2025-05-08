package roomescape.login.business.service;

import org.springframework.stereotype.Service;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.global.auth.jwt.Token;
import roomescape.global.exception.impl.BadRequestException;
import roomescape.global.exception.impl.NotFoundException;
import roomescape.login.presentation.request.LoginCheckRequest;
import roomescape.login.presentation.request.LoginRequest;
import roomescape.login.presentation.request.SignupRequest;
import roomescape.login.presentation.response.LoginCheckResponse;
import roomescape.member.business.domain.Member;
import roomescape.member.business.repository.MemberDao;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtHandler jwtHandler;

    public LoginService(final MemberDao memberDao, final JwtHandler jwtHandler) {
        this.memberDao = memberDao;
        this.jwtHandler = jwtHandler;
    }

    public Token login(final LoginRequest loginRequest) {
        final Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new NotFoundException("회원 정보가 존재하지 않습니다."));

        return jwtHandler.createToken(member);
    }

    public LoginCheckResponse checkLogin(final LoginCheckRequest request) {
        final Member member = memberDao.findById(request.id())
                .orElseThrow(() -> new NotFoundException("회원 정보가 존재하지 않습니다."));

        return new LoginCheckResponse(member.getName());
    }

    public LoginCheckResponse signup(final SignupRequest request) {
        Member member = request.toMember();
        validateDuplicatedEmail(member);
        Member savedMember = memberDao.save(member);
        return LoginCheckResponse.from(savedMember);
    }

    private void validateDuplicatedEmail(Member member) {
        memberDao.findByEmail(member.getEmail())
                .ifPresent((existsMember) -> {
                            throw new BadRequestException("이미 존재하는 이메일입니다.");
                        }
                );
    }
}

