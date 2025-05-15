package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.dto.request.SignupRequest;
import roomescape.member.dto.response.SignupResponse;
import roomescape.member.exception.MemberDuplicatedException;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberService(final PasswordEncoder passwordEncoder, final MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public SignupResponse signup(final SignupRequest signupRequest) {
        String encodedPassword = passwordEncoder.encode(signupRequest.password());
        if (memberRepository.existsByEmail(signupRequest.email())) {
            throw new MemberDuplicatedException("이미 존재하는 회원입니다.");
        }

        Member member = memberRepository.save(
                Member.withUnassignedId(signupRequest.name(), signupRequest.email(), encodedPassword,
                        MemberRole.USER));
        return SignupResponse.from(member);
    }

    public List<MemberResponse> findAllUsers() {
        List<Member> members = memberRepository.findAllUsers();
        return members.stream()
                .map(member -> new MemberResponse(member.getId(), member.getName()))
                .toList();
    }
}
