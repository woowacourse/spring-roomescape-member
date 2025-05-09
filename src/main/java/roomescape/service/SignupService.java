package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.member.RegistrationRequest;
import roomescape.repository.MemberRepository;

@Service
public class SignupService {

    private final MemberRepository memberRepository;

    public SignupService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signup(RegistrationRequest registrationRequest) {
        memberRepository.save(registrationRequest.createRegistrationDetails());
    }
}
