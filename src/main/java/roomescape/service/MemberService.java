package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.request.UserSignUpRequest;
import roomescape.controller.response.CheckMemberResponse;
import roomescape.controller.response.MemberResponse;
import roomescape.controller.response.TokenResponse;
import roomescape.domain.Member;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse save(UserSignUpRequest userSignUpRequest) {
        Member member = userSignUpRequest.toEntity();
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }

    public TokenResponse createToken(UserLoginRequest userLoginRequest) {
        Member member = memberRepository.findByEmail(userLoginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        return new TokenResponse(jwtTokenProvider.createToken(member));
    }

    public CheckMemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return new CheckMemberResponse(member.getName());
    }

    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
