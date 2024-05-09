package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<MemberResponse> login(LoginRequest loginRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmail(new Email(loginRequest.email()));
        if (memberOptional.isPresent() && memberOptional.get().isMatchPassword(loginRequest.password())) {
            return Optional.of(new MemberResponse(memberOptional.get()));
        }
        return Optional.empty();
    }

    public Optional<MemberResponse> findMemberByEmail(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(new Email(email));
        if (memberOptional.isPresent()) {
            Member findMember = memberOptional.get();
            return Optional.of(new MemberResponse(findMember));
        }
        return Optional.empty();
    }

    public Optional<MemberResponse> findById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member findMember = memberOptional.get();
            return Optional.of(new MemberResponse(findMember));
        }
        return Optional.empty();
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
