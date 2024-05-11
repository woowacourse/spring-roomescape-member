package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.exception.AuthFailException;
import roomescape.dto.MemberModel;
import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.request.MemberFindRequest;
import roomescape.dto.response.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberModel readMember(MemberFindRequest request) {
        Member member = memberDao.readMemberByEmailAndPassword(request.email(), request.password())
                .orElseThrow(AuthFailException::new);

        return MemberModel.from(member);
    }

    public List<MemberResponse> readMember() {
        return memberDao.readMember()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse readMember(Long id) {
        Member member = memberDao.readMemberById(id)
                .orElseThrow(AuthFailException::new);

        return MemberResponse.from(member);
    }

    public void createMember(MemberCreateRequest request) {
        Member member = request.createMember();
        memberDao.createMember(member, request.password());
    }
}
