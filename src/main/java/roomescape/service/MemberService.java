package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.dto.request.SignUpRequestDto;
import roomescape.exception.BadRequestException;
import roomescape.repository.MemberRepository;

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
        return memberRepository.save(member);
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
