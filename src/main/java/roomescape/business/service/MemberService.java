package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberEmail;
import roomescape.business.domain.member.MemberName;
import roomescape.presentation.dto.MemberRequest;
import roomescape.presentation.dto.MemberResponse;
import roomescape.presentation.exception.BadRequestException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(final MemberRequest member) {
        final MemberName name = new MemberName(member.name());
        if (memberRepository.existsByName(name)) {
            throw new BadRequestException("이미 사용중인 이름입니다.");
        }
        final MemberEmail email = new MemberEmail(member.email());
        if (memberRepository.existsByEmail(email)) {
            throw new BadRequestException("이미 사용중인 이메일입니다.");
        }
        final Member savedMember = memberRepository.save(
                Member.register(name, email, member.password()));
        return new MemberResponse(savedMember);
    }
}
