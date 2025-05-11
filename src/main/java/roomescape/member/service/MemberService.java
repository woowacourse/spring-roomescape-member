package roomescape.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.ErrorCode;
import roomescape.common.exception.NotFoundException;
import roomescape.member.auth.dto.MemberInfo;
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

    public MemberInfo create(SignupRequest signupRequest) {
        if (memberRepository.existsByEmail(MemberEmail.from(signupRequest.email()))) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        return MemberConverter.toDto(memberRepository.save(
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
        ).orElseThrow(() -> new NotFoundException("등록된 이메일이 존재하지 않습니다."));
    }

    public Member get(MemberId id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("등록된 회원이 아닙니다.", ErrorCode.MEMBER_NOT_FOUND));
    }

    public List<MemberInfo> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberConverter::toDto)
                .toList();
    }
}
