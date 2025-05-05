package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.exception.custom.reason.member.MemberEmailConflictException;
import roomescape.member.dto.MemberRequest;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(
            final MemberRepository memberRepository
    ) {
        this.memberRepository = memberRepository;
    }

    public void createMember(final MemberRequest request) {
        validateDuplicationEmail(request);

        final Member notSavedMember = new Member(request.email(), request.password(), request.name());
        memberRepository.saveMember(notSavedMember);
    }

    private void validateDuplicationEmail(final MemberRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberEmailConflictException();
        }
    }
}
