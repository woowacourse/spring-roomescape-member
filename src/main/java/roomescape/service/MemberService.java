package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional
    public MemberResponse addMember(MemberRequest memberRequest) {
        Member member = memberRequest.toMember();

        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("해당 email의 사용자가 이미 존재합니다.");
        }

        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }

    @Transactional
    public void deleteMemberById(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new NoSuchElementException("해당 id의 사용자가 존재하지 않습니다.");
        }

        memberRepository.deleteById(id);
    }
}
