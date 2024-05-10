package roomescape.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequestDto;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findById(final Long id) {
        return memberDao.getById(id);
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }

    public long save(final MemberRequestDto memberRequestDto) {
        return memberDao.save(memberRequestDto.toMember());
    }
}
