package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberPassword;
import roomescape.dto.member.MemberLoginRequest;
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

    public String createToken(MemberLoginRequest request) {
        checkInvalidLogin(request);
        return jwtTokenProvider.createToken(findMember(request));
    }

    private Member findMember(MemberLoginRequest request) {
        return memberDao.readByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    private Member findMember(Long id) {
        return memberDao.readById(id)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    public Member findMemberByToken(String token) {
        Long payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public boolean isAdminByToken(String token) {
        return findMemberByToken(token).isAdmin();
    }

    private void checkInvalidLogin(MemberLoginRequest request) {
        Member member = memberDao.readByEmail(request.email())
                .orElseThrow(() -> new AuthorizationException("아이디와 비밀번호를 확인해주세요."));
        if (member.isNotSame(new MemberPassword(request.password()))) {
            throw new AuthorizationException("아이디와 비밀번호를 확인해주세요.");
        }
    }
}
