package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.MemberRequest;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveMember(final MemberRequest request) {
        memberRepository.save(new Member(request.name(), request.email(), request.password()));
    }
}
