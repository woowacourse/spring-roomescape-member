package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.controller.request.SignUpRequest;
import roomescape.member.domain.Member;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(SignUpRequest request) {
        String name = request.name();
        String email = request.email();
        String password = request.password();
        return memberRepository.save(name, email, password);
    }

    public Member findById(Long id) {
        return memberRepository.findMemberById(id);
    }

    public Member findByName(String name) {
        return memberRepository.findMemberByName(name);
    }
}
