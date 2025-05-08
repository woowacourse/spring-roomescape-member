package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.RegistrationRequestDto;
import roomescape.repository.MemberRepository;

@Service
public class SignupService {

    private final MemberRepository memberRepository;

    public SignupService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signup(RegistrationRequestDto registrationRequestDto) {
        memberRepository.save(registrationRequestDto.createRegistrationDetails());
    }
}
