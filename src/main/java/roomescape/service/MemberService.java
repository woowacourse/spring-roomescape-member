package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.error.MemberException;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveMember(final MemberRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberException("이미 존재하는 이메일입니다.");
        }
        memberRepository.save(new Member(null, request.name(), request.email(), request.password(), Role.USER));
    }

    public List<MemberResponse> findAllMember() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::new)
                .toList();
    }
}
