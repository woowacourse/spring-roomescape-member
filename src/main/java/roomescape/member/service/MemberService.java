package roomescape.member.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.controller.dto.SignupRequest;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberId;
import roomescape.member.domain.MemberName;
import roomescape.member.domain.MemberPassword;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signup(SignupRequest signupRequest) {
        if (memberRepository.existsByEmail(MemberEmail.from(signupRequest.email()))) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        return memberRepository.save(
                Member.withoutId(
                        MemberName.from(signupRequest.name()),
                        MemberEmail.from(signupRequest.email()),
                        MemberPassword.from(signupRequest.password()),
                        Role.MEMBER
                )
        );
    }

    public Member findMember(LoginRequest loginRequest) {
        return memberRepository.findByParams(
                MemberEmail.from(loginRequest.email()),
                MemberPassword.from(loginRequest.password())
        ).orElseThrow(() -> new NoSuchElementException("등록된 이메일이 없거나 비밀번호를 잘못 입력하였습니다.:"));
    }

    public Member findMemberById(MemberId id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("등록된 회원이 아닙니다."));
    }
}
