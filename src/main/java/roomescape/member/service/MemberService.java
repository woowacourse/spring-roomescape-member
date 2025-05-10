package roomescape.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.global.error.exception.NotFoundException;
import roomescape.member.dto.request.MemberRequest.MemberCreateRequest;
import roomescape.member.dto.response.MemberResponse.MemberCreateResponse;
import roomescape.member.dto.response.MemberResponse.MemberReadResponse;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberCreateResponse createMember(MemberCreateRequest request) {
        Member member = request.toEntity();
        Member saved = memberRepository.save(member);
        return MemberCreateResponse.from(saved);
    }

    public List<MemberReadResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberReadResponse::from)
                .toList();
    }

    public void deleteMember(long id) {
        boolean deleted = memberRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }
}
