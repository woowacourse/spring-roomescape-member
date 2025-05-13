package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.member.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.response.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public Member findById(final Long id) {
        final Optional<Member> member = memberDao.findById(id);
        if (member.isEmpty()) {
            throw new NoSuchElementException("멤버 정보를 찾을 수 없습니다.");
        }
        return member.get();
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberDao.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsById(final Long id) {
        return memberDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(final String email) {
        return memberDao.findByEmail(email);
    }
}
