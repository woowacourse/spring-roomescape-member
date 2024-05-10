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

    public MemberResponse login(LoginRequest loginRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmail(new Email(loginRequest.email()));
        if (memberOptional.isPresent() && memberOptional.get().isMatchPassword(loginRequest.password())) {
            return new MemberResponse(memberOptional.get());
        }
        throw new IllegalArgumentException("요청을 처리할 수 없습니다. 로그인 정보를 다시 확인해주세요.");
    }

    public MemberResponse findMemberByEmail(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(new Email(email));
        if (memberOptional.isPresent()) {
            Member findMember = memberOptional.get();
            return new MemberResponse(findMember);
        }
        throw new IllegalArgumentException("요청을 처리할 수 없습니다. 이메일을 다시 확인해주세요.");
    }

    public MemberResponse findById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member findMember = memberOptional.get();
            return new MemberResponse(findMember);
        }
        throw new IllegalArgumentException("요청을 처리할 수 없습니다. 회원ID를 다시 확인해주세요.");
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
