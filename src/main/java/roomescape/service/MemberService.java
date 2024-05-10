package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.repository.MemberDao;

import java.util.List;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    // TODO dto 반환으로 일관화 시키기
    public Member findMemberById(Long id) {
        return findById(id);
    }

    public Member findMemberByEmailAndPassword(final String email, final String password) {
        return findByEmailAndPassword(email, password);
    }

    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }

    private Member findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "[ERROR] " + email + " 을 가진 회원이 존재하지 않거나 비밀번호가 일치하지 않습니다.")
                );
    }

    private Member findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] 멤버 id는 null일 수 없습니다.");
        }
        return memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 멤버를 찾을 수 없습니다."));
    }
}
