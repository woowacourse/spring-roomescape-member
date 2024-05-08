package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberPassword;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
import roomescape.exception.AuthorizationException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse add(MemberSignupRequest signupRequest) {
        Member member = signupRequest.toDomain();
        validateNotExistEmail(member.getEmail());
        Member createdMember = memberDao.create(member);
        return new MemberResponse(createdMember);
    }

    public void checkLoginInfo(LoginRequest request) {
        MemberEmail email = new MemberEmail(request.getEmail());
        MemberPassword password = new MemberPassword(request.getPassword());
        if (!memberDao.existByEmailAndMemberPassword(email, password)) {
            throw new AuthorizationException("아이디 또는 비밀번호 오류입니다.");
        }
    }

    private void validateNotExistEmail(MemberEmail memberEmail) {
        if (memberDao.existByEmail(memberEmail)) {
            throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
        }
    }
}
