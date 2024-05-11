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

    // TODO - 중복된 email이 존재하면 예외 발생
    public Member join(final MemberRequest memberRequest) {
        Member member = memberRequest.toEntity();
        return memberDAO.insert(member);
    }

    public List<Member> findAll() {
        return memberDAO.findAll();
    }
}
