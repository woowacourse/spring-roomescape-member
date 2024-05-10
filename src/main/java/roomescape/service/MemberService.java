package roomescape.service;

import java.util.List;
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

    public List<MemberResponse> findAll() {
        List<Member> members = memberDao.readAll();
        return members.stream()
                .map(MemberResponse::new)
                .toList();
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

    public Member findAuthInfo(String payload) {
        MemberEmail email = new MemberEmail(payload);
        return findByEmail(email);
    }

    private void validateNotExistEmail(MemberEmail memberEmail) {
        if (memberDao.existByEmail(memberEmail)) {
            throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
        }
    }

    private Member findByEmail(MemberEmail email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("이메일에 해당하는 회원 정보가 존재하지 않습니다."));
    }
}
