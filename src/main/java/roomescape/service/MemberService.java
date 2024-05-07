package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.Role;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse createMember(SignupRequest request) {
        Member member = new Member(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name(),
                Role.USER
        );

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }
}
