package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.MemberDao;
import roomescape.dto.AuthorizationResponse;
import roomescape.dto.LoginRequest;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;
import roomescape.exception.InvalidAccessTokenException;
import roomescape.exception.MemberNotFoundException;

@Component
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void validateMemberExistence(LoginRequest login) {
        boolean hasMember = memberDao.existsByEmailAndPassword(login.email(), login.password());
        if (!hasMember) {
            //TODO : signup으로 넘어가나?
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }

    public AccessToken createAccessToken(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        return new AccessToken(member);
    }

    public AuthorizationResponse findMember(AccessToken accessToken) {
        //TODO : 왜 바로 name을 안 꺼내오고 id를 가져오는가? -> 식별자를 가져오는 것이 더 안전하지 않을까? 그리고 name을 가져오기로 했다가 바뀔수도 있다.
        try {
            long memberId = accessToken.findSubject();
            Member member = memberDao.findById(memberId);
            return new AuthorizationResponse(member);
        } catch (MemberNotFoundException exception) {
            // TODO DAO에서 안잡고 여기서 잡은 이유 -> dao에서 잡기엔 너무 에러메세지가 authroization로직에서만 유효한 잡기이다.
            throw new InvalidAccessTokenException();
        }
    }
}
