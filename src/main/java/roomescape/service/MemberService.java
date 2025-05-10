package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.dto.request.MemberCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.member.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllByRole(MemberRole role) {
        return memberRepository.findAllByRole(role);
    }

    public Member getById(long id) {
        return loadById(id);
    }

    public long addMember(MemberCreationRequest request) {
        validateDuplicatedEmail(request.email());
        Member member = Member.createWithoutId(
                request.name(), request.email(), request.password(), MemberRole.GENERAL);
        return memberRepository.add(member);
    }

    private Member loadById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("[ERROR] 존재하지 않는 회원입니다."));
    }

    private void validateDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BadRequestException("[ERROR] 이미 존재하는 계정입니다.");
        }
    }
}
