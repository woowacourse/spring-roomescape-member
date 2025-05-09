package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtProvider;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.entity.LoginMember;
import roomescape.exception.AuthenticationException;
import roomescape.repository.MemberDao;


@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public LoginResponse login(LoginRequest request) {
        LoginMember loginMember = memberDao.findByEmailAndPassword(request.email(), request.password())
            .orElseThrow(() -> new AuthenticationException("로그인 정보를 찾을 수 없습니다."));

        String accessToken = JwtProvider.generateToken(loginMember);
        return LoginResponse.from(accessToken);
    }

    public LoginCheckRequest findById(Long memberId) {
        LoginMember findLoginMember = memberDao.findById(memberId)
            .orElseThrow(() -> new AuthenticationException("로그인 정보가 일치하지 않습니다."));
        return LoginCheckRequest.of(findLoginMember.getId(), findLoginMember.getName(), findLoginMember.getEmail(), findLoginMember.getRole());
    }
}
