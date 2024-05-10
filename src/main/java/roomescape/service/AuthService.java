package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.member.UserLoginRequest;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.exception.AuthorizationException;
import roomescape.service.exception.InvalidRequestException;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member findMember(UserLoginRequest request) {
        return memberDao.readByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    public Member findMember(Long id) {
        return memberDao.readById(id)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    public Member findMemberByToken(String token) {
        Long payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public String createToken(UserLoginRequest request) {
        if (checkInvalidLogin(request)) {
            throw new AuthorizationException("이메일과 비밀번호를 확인해 주세요.");
        }
        return jwtTokenProvider.createToken(findMember(request));
    }

    private boolean checkInvalidLogin(UserLoginRequest request) {
        return memberDao.readByEmailAndPassword(request.email(), request.password())
                .isEmpty();
    }
}
