package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginCheckResponse;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.util.TokenProvider;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String checkLogin(LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다.");
        }

        return tokenProvider.createToken(loginRequest.getEmail());
    }

    private boolean checkInvalidLogin(String email, String password) {
        return !memberRepository.existBy(email, password);
    }

    public LoginCheckResponse findMemberNameByToken(String token) {
        String email = tokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(email);
        validateExistMember(member);
        return new LoginCheckResponse(member.getName());
    }

    public Object findLoginMemberByToken(String token) {
        String email = tokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(email);
        validateExistMember(member);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    private void validateExistMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
