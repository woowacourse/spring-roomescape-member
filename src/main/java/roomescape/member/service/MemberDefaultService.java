package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.auth.exception.LoginFailException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberDefaultService implements MemberService {
    private final MemberRepository memberRepository;

    public MemberDefaultService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberResponse create(MemberRequest request) {
        Member member = Member.createWithoutId(request.name(), request.email(), request.password());
        return MemberResponse.from(memberRepository.add(member));
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findIdByEmailAndPassword(email, password)
                .orElseThrow(LoginFailException::new);
    }
}
