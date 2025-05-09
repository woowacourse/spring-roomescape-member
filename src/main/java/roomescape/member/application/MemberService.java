package roomescape.member.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.AuthRole;
import roomescape.exception.AuthorizationException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.ui.dto.CreateMemberRequest;
import roomescape.member.ui.dto.MemberNameResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void create(final CreateMemberRequest request) {
        final Member member = new Member(
                request.name(),
                request.email(),
                request.password(),
                AuthRole.MEMBER
        );
        memberRepository.save(member);
    }

    public void delete(final Long id) {
        final Member found = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 예약 데이터가 존재하지 않습니다. id = " + id));

        if (found.getRole() == AuthRole.ADMIN) {
            throw new AuthorizationException("관리자는 삭제할 수 없습니다.");
        }
        memberRepository.deleteById(id);
    }

    public List<MemberNameResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberNameResponse::from)
                .toList();
    }
}
