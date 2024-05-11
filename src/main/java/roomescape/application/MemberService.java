package roomescape.application;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.TokenCreationRequest;
import roomescape.application.dto.response.MemberResponse;
import roomescape.application.dto.response.TokenResponse;
import roomescape.auth.TokenProvider;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Service
public class MemberService {
    private static final String WRONG_EMAIL_OR_PASSWORD_MESSAGE = "등록되지 않은 이메일이거나 비밀번호가 틀렸습니다.";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse authenticateMember(TokenCreationRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE));
        validatePassword(member, request.password());
        String token = tokenProvider.createToken(member.getId());
        return new TokenResponse(token);
    }

    private void validatePassword(Member member, String password) {
        if (!member.matchPassword(password)) {
            throw new IllegalArgumentException(WRONG_EMAIL_OR_PASSWORD_MESSAGE);
        }
    }

    public MemberResponse getMemberById(long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return new MemberResponse(member.getName());
    }

    public List<MemberResponse> findMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
