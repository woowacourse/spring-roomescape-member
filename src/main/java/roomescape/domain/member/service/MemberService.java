package roomescape.domain.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.dto.request.MemberRequestDto;
import roomescape.domain.member.dto.response.MemberResponseDto;
import roomescape.domain.member.dto.response.MemberResponseDtoOfNames;
import roomescape.domain.member.model.Member;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = Member.createUser(memberRequestDto.name(), memberRequestDto.email(),
            memberRequestDto.password());
        long savedId = memberDao.save(member);
        member.setId(savedId);
        return MemberResponseDto.from(member);
    }

    public Member getMemberOf(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public Member getMemberFrom(String email) {
        return memberDao.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public List<MemberResponseDtoOfNames> getAllMembers() {
        return memberDao.findAll().stream()
            .map(MemberResponseDtoOfNames::from)
            .toList();
    }
}
