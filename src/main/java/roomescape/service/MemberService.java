package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.request.CreateMemberRequest;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> signUp(CreateMemberRequest request) {
        Member member = request.toEntity();
        if (memberRepository.existsByEmail(member)) {
            return Optional.empty();
        }

        return Optional.ofNullable(memberRepository.save(member));
    }

    public List<Member> readMembers() {
        return memberRepository.readAll();
    }
}
