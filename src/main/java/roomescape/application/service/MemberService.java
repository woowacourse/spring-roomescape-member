package roomescape.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.MemberResponse;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> getMembers() {
        return memberDao.findAll().stream()
                .map(MemberResponse::new)
                .toList();
    }

    public Member getMemberById(Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다."));
    }

    public Member getMemberByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다."));
    }
}
