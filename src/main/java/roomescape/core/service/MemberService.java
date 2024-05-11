package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Member;
import roomescape.core.domain.Role;
import roomescape.core.dto.SignUpRequestDto;
import roomescape.core.repository.MemberRepository;
import roomescape.web.exception.BadRequestException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member create(final SignUpRequestDto request) {
        final Member member = new Member(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                Role.MEMBER
        );
        validateDuplicatedEmail(member.getEmail());
        final Long id = memberRepository.save(member);
        return new Member(
                id,
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                Role.MEMBER
        );
    }

    @Transactional(readOnly = true)
    public List<Member> findAllRoleMembers() {
        return memberRepository.findAllByRoleMember();
    }

    private void validateDuplicatedEmail(final String email) {
        if (memberRepository.hasDuplicateEmail(email)) {
            throw new BadRequestException("중복된 이메일이 존재합니다.");
        }
    }
}
