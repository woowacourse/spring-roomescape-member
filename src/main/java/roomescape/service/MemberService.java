package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.exception.DuplicatedException;
import roomescape.exception.LoginException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.member.MemberRepository;
import roomescape.service.dto.member.MemberLoginRequest;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.dto.member.MemberSignUpRequest;
import roomescape.service.dto.member.MemberTokenResponse;
import roomescape.utils.TokenManager;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;

    public MemberService(MemberRepository memberRepository, TokenManager tokenManager) {
        this.memberRepository = memberRepository;
        this.tokenManager = tokenManager;
    }

    public MemberResponse signUp(MemberSignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new DuplicatedException("이미 가입되어 있는 이메일 주소입니다.");
        }
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.from(member);
    }

    public MemberTokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginException("이메일 혹은 비밀번호가 잘못되었습니다."));
        checkPasswordMatches(member, request.password());
        return tokenManager.generateToken(member);
    }

    private void checkPasswordMatches(Member member, String password) {
        if (!member.isPasswordMatches(password)) {
            throw new LoginException("이메일 혹은 비밀번호가 잘못되었습니다.");
        }
    }

    public Member get(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 사용자가 없습니다."));
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
