package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.SignupRequest;
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
        return memberOptional
                .filter(member -> member.isMatchPassword(loginRequest.password()))
                .map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 없습니다. 로그인 정보를 다시 확인해주세요."));
    }

    public MemberResponse create(SignupRequest signupRequest) {
        Member member = new Member(new Name(signupRequest.name()), new Email(signupRequest.email()), Role.USER, signupRequest.password());
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다. 다른 이메일을 입력해주세요."); // TODO 예외 메시지가 유저는 안보임
        }
        Member save = memberRepository.save(member);
        return MemberResponse.from(save);
    }

    public MemberResponse findMemberByEmail(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(new Email(email));
        return memberOptional.map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다. 이메일을 다시 확인해주세요."));
    }

    public MemberResponse findById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        return memberOptional.map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 없습니다. 회원ID를 다시 확인해주세요."));
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
