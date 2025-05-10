package roomescape.member.service;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.controller.dto.SignupRequest;
import roomescape.member.domain.Account;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberId;
import roomescape.member.domain.MemberName;
import roomescape.member.domain.Password;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public roomescape.member.controller.dto.MemberInfo signup(SignupRequest signupRequest) {
        if (memberRepository.existsByEmail(MemberEmail.from(signupRequest.email()))) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        return MemberConverter.toResponse(memberRepository.save(
                Account.of(
                        Member.withoutId(
                                MemberName.from(signupRequest.name()),
                                MemberEmail.from(signupRequest.email()),
                                Role.MEMBER
                        ),
                        Password.from(signupRequest.password())
                )
        ));
    }

    public Account findAccount(LoginRequest loginRequest) {
        return memberRepository.findAccountByEmail(MemberEmail.from(loginRequest.email())
        ).orElseThrow(() -> new NoSuchElementException("등록된 이메일이 존재하지 않습니다."));
    }

    public Member get(MemberId id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("등록된 회원이 아닙니다."));
    }

    public List<roomescape.member.controller.dto.MemberInfo> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberConverter::toResponse)
                .toList();
    }
}
