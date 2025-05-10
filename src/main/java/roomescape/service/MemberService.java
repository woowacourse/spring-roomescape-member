package roomescape.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.auth.JwtProvider;
import roomescape.domain.Member;
import roomescape.dto.SignupRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    // TODO: password 암호화
    public String login(LoginRequest request) {
        Member member = memberRepository.getByEmail(request.email());
        if (!Objects.equals(member.password(), request.password())) {
            throw new IllegalArgumentException("잘못된 아이디 또는 패스워드입니다");
        }
        return jwtProvider.createToken(member);
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
