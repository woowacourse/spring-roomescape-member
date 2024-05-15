package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.exception.InvalidInputException;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        return memberDao.findAll()
                .stream()
                .map(MemberResponse::fromMember)
                .toList();
    }

    public Member findMember(LoginRequest loginRequest) {
        Member member = new Member(loginRequest.email(), loginRequest.password());
        return memberDao.find(member)
                .orElseThrow(() -> new InvalidInputException("아이디 혹은 비밀번호가 잘못되었습니다."));
    }

    public Member findMember(MemberRequest memberRequest) {
        return memberDao.findById(memberRequest.id())
                .orElseThrow(() -> new InvalidInputException("해당 계정이 존재하지 않습니다."));
    }
}
