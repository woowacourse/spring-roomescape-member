package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dto.request.MemberPostRequest;
import roomescape.dto.response.MemberSafeResponse;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;
import roomescape.web.LoginMember;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberSafeResponse> findAllMembers() {
        return memberDao.findAll().stream()
                .map(MemberSafeResponse::new)
                .toList();
    }

    public MemberSafeResponse createUser(MemberPostRequest request) {
        Member memberWithoutId = request.toMember(MemberRole.USER);

        if (memberDao.existsByEmail(memberWithoutId)) {
            throw new IllegalArgumentException("해당 이메일로 가입한 이력이 있습니다.");
        }

        Member member = memberDao.create(memberWithoutId);
        return new MemberSafeResponse(member);
    }

    /**
     * 아직 사용하지 않는 메서드입니다. 회원 정보 변경 시 토큰 즉시 무효화를 연습하기 위한 메서드입니다.
     */
    public void deleteUser(LoginMember loginMember) {
        memberDao.deleteById(loginMember.id());
    }
}
