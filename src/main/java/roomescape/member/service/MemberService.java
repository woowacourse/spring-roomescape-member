package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.SignUpRequest;
import roomescape.member.dto.SignUpResponse;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.member.repository.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public SignUpResponse registerUser(SignUpRequest request) {
        Member member = Member.withoutId(request.getEmail(), request.getPassword(), request.getName(), Role.USER);
        Member registeredUser = memberRepository.save(member);
        return SignUpResponse.from(registeredUser);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberResponse(member.getId(), member.getName(), member.getEmail()))
                .toList();
    }
}
