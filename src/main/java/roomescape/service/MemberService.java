package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginMember;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.SignUpRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.model.Member;
import roomescape.model.MemberName;
import roomescape.model.Role;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    private final MemberFinder memberFinder;

    public MemberService(final MemberRepository memberRepository, final MemberFinder memberFinder) {
        this.memberRepository = memberRepository;
        this.memberFinder = memberFinder;
    }

    public LoginMember loginMember(LoginMemberRequest loginMemberRequest) {

        Member member = memberRepository.findByEmail(loginMemberRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다"));
        if (!member.getPassword().equals(loginMemberRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return LoginMember.from(member);
    }

    public LoginMember getMemberById(Long id) {
        Member member = memberFinder.getMemberById(id);
        return new LoginMember(member.getId(), member.getRole(), member.getMemberName(),
                member.getEmail(), member.getPassword());
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> {
                    return new MemberResponse(member.getId(), member.getMemberName().getName());
                })
                .toList();
    }

    public MemberResponse signUp(final SignUpRequest signUpRequest) {
        Member member = new Member(null, Role.USER, new MemberName(signUpRequest.name()), signUpRequest.email(),
                signUpRequest.password());
        Member savedMember = memberRepository.saveMember(member);
        return new MemberResponse(savedMember.getId(), savedMember.getMemberName().getName());

    }

    public MemberResponse adminSignUp(final SignUpRequest signUpRequest) {
        Member member = new Member(null, Role.ADMIN, new MemberName(signUpRequest.name()), signUpRequest.email(),
                signUpRequest.password());
        Member savedMember = memberRepository.saveMember(member);
        return new MemberResponse(savedMember.getId(), savedMember.getMemberName().getName());
    }
}
