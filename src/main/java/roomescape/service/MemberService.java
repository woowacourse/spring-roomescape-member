package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;

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

    private void validateNotExistEmail(MemberEmail memberEmail) {
        if (memberDao.existByEmail(memberEmail)) {
            throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
        }
    }
}
