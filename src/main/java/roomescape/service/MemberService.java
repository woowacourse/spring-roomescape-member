package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDAO;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;

import java.util.List;

@Service
public class MemberService {
    private final MemberDAO memberDAO;

    public MemberService(final MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public Member join(final MemberRequest memberRequest) {
        if (memberDAO.existMemberOf(memberRequest.email())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        Member member = memberRequest.toEntity();
        return memberDAO.insert(member);
    }

    public List<Member> findAll() {
        return memberDAO.findAll();
    }
}
