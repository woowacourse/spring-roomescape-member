package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.LoginMember;
import roomescape.dto.MemberLoginRequestDto;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationService authenticationService;

    public MemberService(final MemberRepository memberRepository, final AuthenticationService authenticationService) {
        this.memberRepository = memberRepository;
        this.authenticationService = authenticationService;
    }

    public String loginMember(MemberLoginRequestDto memberLoginRequestDto) {

        Member member = memberRepository.findByEmail(memberLoginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다"));
        if (!member.getPassword().equals(memberLoginRequestDto.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return authenticationService.generateToken(member);

    }

    public LoginMember getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디 해당하는 멤버 없습니다"));
        return new LoginMember(member.getId(), member.getRole(), member.getMemberName(), member.getEmail());
    }
}
