package roomescape.service.member;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.SignupResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(
                        "[ERROR] (email : " + email + ") 에 대한 사용자가 존재하지 않습니다.")
                );
    }

    public SignupResponse save(final SignupRequest signupRequest) {
        Member member = memberDao.save(signupRequest.toEntity());
        return new SignupResponse(member.getNameValue(), member.getEmail(), member.getPassword());
    }
}
