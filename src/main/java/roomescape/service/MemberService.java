package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberPreviewResponse;
import roomescape.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String logIn(LogInRequest logInRequest) {
        String email = logInRequest.email();
        String password = logInRequest.password();
        Member member = getValidatedUserByEmailAndPassword(email, password);

        return jwtTokenProvider.createToken(member);
    }

    private Member getValidatedUserByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException("일치하는 이메일과 비밀번호가 없습니다."));
    }

    public Member getMemberById(Long id) {
        return findValidatedMemberById(id);
    }

    private Member findValidatedMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new ResourceNotFoundException("아이디에 해당하는 사용자가 없습니다."));
    }

    public List<MemberPreviewResponse> getAllMemberPreview() {
        return memberRepository.findAll().stream()
                .map(MemberPreviewResponse::from)
                .toList();
    }
}
