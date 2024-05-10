package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

import java.util.List;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member login(String email, String password) {
        Member member = memberDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!member.isPasswordMatches(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    public Member findByEmail(String email) {
        return memberDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
    }

    public List<Member> getAllMembers() {
        return memberDao.findAll();
    }
}
