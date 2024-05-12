package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.AuthInformationResponse;
import roomescape.member.dto.response.FindMemberResponse;
import roomescape.member.model.Member;
import roomescape.member.model.Role;
import roomescape.member.repositoy.JdbcMemberRepository;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;

    public MemberService(JdbcMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    Member createMember(Member member) {
        return memberRepository.save(member); // TODO: 패스워드 암호화
    }

    public List<FindMemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(FindMemberResponse::of)
                .toList();
    }

    public AuthInformationResponse getAuthInformation(Long id) {
        String name = getMember(id).getName();
        return new AuthInformationResponse(name);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));
    }

    public Role getMemberRoleById(final Long memberId) {
        return memberRepository.findById(memberId)
                .map(Member::getRole)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));
    }
}
