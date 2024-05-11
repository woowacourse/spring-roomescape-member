package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dto.MemberLoginCheckResponse;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberLoginCheckResponse findLoginMemberInfo(long id) {
        String name = memberRepository.findNameById(id);
        return new MemberLoginCheckResponse(name);
    }
}
