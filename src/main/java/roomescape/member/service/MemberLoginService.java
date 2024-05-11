package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.member.dao.MemberJdbcDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberLoginRequest;

@Service
public class MemberLoginService {

    private final MemberJdbcDao memberJdbcDao;

    public MemberLoginService(MemberJdbcDao memberJdbcDao) {
        this.memberJdbcDao = memberJdbcDao;
    }

    public Member findMember(MemberLoginRequest memberLoginRequest) {

        Member member = memberJdbcDao.findByEmail(memberLoginRequest.email());
        validateLoginRequest(member);
        return member;
    }

    private void validateLoginRequest(Member member) {
        if (member == null || member.getEmail() == null || member.getPassword() == null) {
            throw new BadRequestException("등록되지 않은 회원입니다.");
        }
    }

}
