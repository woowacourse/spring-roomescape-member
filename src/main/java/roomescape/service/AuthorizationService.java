package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.MemberDao;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberNameResponse;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;
import roomescape.exception.InvalidAccessTokenException;
import roomescape.exception.MemberNotFoundException;
import roomescape.web.LoginMember;

@Component
public class AuthorizationService {

    private final MemberDao memberDao;

    public AuthorizationService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void validateMemberExistence(LoginRequest login) {
        boolean hasMember = memberDao.existsByEmailAndPassword(login.email(), login.password());
        if (!hasMember) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }

    public MemberNameResponse findAuthorizedMember(LoginMember loginMember) {
        try {
            Member member = memberDao.findById(loginMember.getId());
            return new MemberNameResponse(member);
        } catch (MemberNotFoundException exception) {
            throw new InvalidAccessTokenException();
        }
    }

    public AccessToken createAccessToken(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        return new AccessToken(member);
    }
}
