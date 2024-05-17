package roomescape.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;
import roomescape.dto.member.SignupRequest;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberInfo> findAll() {
        return memberDao.findAll();
    }

    public MemberInfo insertMember(SignupRequest signupRequest) {
        if (memberDao.isEmailExist(signupRequest.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member member = new Member(null, signupRequest.name(), signupRequest.email(), signupRequest.password(),
                Role.USER);

        return memberDao.insert(member);
    }
}
