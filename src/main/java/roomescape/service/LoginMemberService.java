package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberRepository;
import roomescape.domain.Member;
import roomescape.exception.AuthorizationException;
import roomescape.service.dto.MemberResponse;
import roomescape.service.dto.TokenRequest;

@Service
public class LoginMemberService {
    private final MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::new)
                .toList();
    }

    public MemberResponse findById(long id) {
        return new MemberResponse(memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다.")));
    }

    public void validateLogin(TokenRequest tokenRequest) {
        Member member = findByEmail(tokenRequest);
        validatePassword(tokenRequest, member);
    }

    private Member findByEmail(TokenRequest tokenRequest) {
        return memberRepository.findByEmail(tokenRequest.email())
                .orElseThrow(() -> new AuthorizationException("잘못된 이메일입니다."));
    }

    private void validatePassword(TokenRequest tokenRequest, Member member) {
        if (!member.getPassword().equals(tokenRequest.password())) {
            throw new AuthorizationException("잘못된 비밀번호입니다.");
        }
    }
}
