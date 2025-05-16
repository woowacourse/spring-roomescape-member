package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.api.member.dto.LoginCheckResponse;
import roomescape.controller.api.member.dto.MemberLoginRequest;
import roomescape.controller.api.member.dto.MemberResponse;
import roomescape.controller.api.member.dto.MemberSignupRequest;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(final MemberRepository memberRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(final MemberLoginRequest request) {
        final Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthorizationException("해당 이메일에 대한 회원 정보가 존재하지 않습니다. 이메일: " + request.email()));
        if (!member.isPasswordMatched(request.password())) {
            throw new AuthorizationException("비밀번호가 틀립니다.");
        }
        return jwtTokenProvider.createToken(member);
    }

    public LoginCheckResponse checkLogin(final Member member) {
        if (member == null) {
            throw new AuthorizationException("회원 정보가 존재하지 않습니다.");
        }
        return new LoginCheckResponse(member.name());
    }

    public MemberResponse add(final MemberSignupRequest request) {
        final Member member = request.toEntity();
        final Long id = memberRepository.save(member);
        final Member savedMember = new Member(id, member.name(), member.email(), member.password(), member.role());
        return MemberResponse.from(savedMember);
    }

    public List<MemberResponse> findAll() {
        final List<Member> members = memberRepository.findAll();
        return MemberResponse.from(members);
    }

    public MemberResponse findByToken(final String token) {
        final boolean isValidToken = jwtTokenProvider.validateToken(token);
        if (!isValidToken) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
        final String email = jwtTokenProvider.getPayload(token);
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("회원 정보가 존재하지 않습니다."));
        return MemberResponse.from(member);
    }

    public Boolean isValidToken(final String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
