package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.UserSignUpRequest;
import roomescape.domain.User;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void save(UserSignUpRequest userSignUpRequest) {
        User user = userSignUpRequest.toEntity();

        memberRepository.save(user);
    }
}
