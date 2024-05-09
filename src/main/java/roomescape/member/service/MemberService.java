package roomescape.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.controller.dto.SignUpRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberSignUp;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse create(SignUpRequest signUpRequest) {
        Member member = memberRepository.save(
                new MemberSignUp(signUpRequest.name(), signUpRequest.email(), signUpRequest.password(), Role.USER));
        return new MemberResponse(member.getId(), member.getName());
    }
}
