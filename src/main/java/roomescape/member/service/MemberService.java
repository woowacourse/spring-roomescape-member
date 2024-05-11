package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.AuthInformationResponse;
import roomescape.member.model.Member;
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

    public AuthInformationResponse getMemberNameById(Long id) {
        String name = getMemberById(id).getName();
        return new AuthInformationResponse(name);
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));
    }
}
