package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.RegistrationRequestDto;
import roomescape.dto.TokenResponseDto;
import roomescape.repository.MemberRepository;
import roomescape.util.JwtTokenProvider;

@Service
public class SignupService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponseDto signup(RegistrationRequestDto registrationRequestDto) {
        memberRepository.save(registrationRequestDto.createRegistrationDetails());
        String token = jwtTokenProvider.createToken(registrationRequestDto.email());
        return new TokenResponseDto(token);
    }
}
