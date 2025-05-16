package roomescape.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.auth.JwtProvider;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public String login(LoginRequest request) {
        Optional<Member> member = memberRepository.findByEmail(request.email());
        if (member.isEmpty() || !Objects.equals(member.get().password(), request.password())) {
            throw new IllegalArgumentException("잘못된 아이디 또는 비밀번호입니다");
        }
        return jwtProvider.createToken(member.get());
    }

    public LoginCheckResponse loginCheck(Member member) {
        return LoginCheckResponse.from(member);
    }

    public void signup(SignupRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        memberRepository.save(new Member(request.name(), request.email(), request.password()));
    }

    public List<MemberResponse> getAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
