package roomescape.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;
import roomescape.auth.service.dto.SignUpRequest;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Autowired
    public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.getByEmail(loginRequest.email());
        String token = tokenProvider.create(member);
        return new LoginResponse(token);
    }

    public void signUp(SignUpRequest signUpRequest) {
        //TODO: email 중복 검사
        memberRepository.save(new Member(signUpRequest.name(), signUpRequest.email(), signUpRequest.password(), Role.GUEST));
    }
}
