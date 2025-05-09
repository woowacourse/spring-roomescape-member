package roomescape.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.enums.Role;
import roomescape.dto.login.LoginRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.signup.SignupRequest;
import roomescape.exception.member.MemberNotFoundException;
import roomescape.repository.member.MemberRepository;
import roomescape.util.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAllMembers() {
        List<Member> members = memberRepository.findAllMembers();
        return members.stream().map(member -> MemberResponse.of(member)).toList();
    }

    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    public MemberResponse addMember(SignupRequest signupRequest) {
        Member member = new Member(null, signupRequest.name(), signupRequest.email(), signupRequest.password(),
                Role.USER);
        Long id = memberRepository.addMember(member);
        Member addedMember = member.withId(id);
        return MemberResponse.of(addedMember);
    }

    public String createToken(LoginRequest loginRequest) {
        Member foundMember = memberRepository.findMemberByEmailAndPassword(loginRequest.toMember());
        String token = JwtTokenProvider.createToken(foundMember);
        return token;
    }
}
