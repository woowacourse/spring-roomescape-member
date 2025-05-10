package roomescape.member.application.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.global.exception.InvalidMemberException;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.infrastructure.JwtTokenProvider;
import roomescape.member.presentation.dto.LoginRequest;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.member.presentation.dto.RegisterRequest;
import roomescape.member.presentation.dto.TokenResponse;

@Service
public class MemberService {

    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidMemberException("이메일이 올바르지 않습니다.", HttpStatus.NOT_FOUND));
        validateUserLogin(member, loginRequest);
        return tokenProvider.createToken(member);
    }

    public Member getMember(String token) {
        Long id = tokenProvider.resolveTokenToMemberId(token);
        return memberRepository.findById(id)
                .orElseThrow(() -> new InvalidMemberException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
    }

    public MemberNameResponse signup(RegisterRequest registerRequest) {
        CreateMemberRequest request = new CreateMemberRequest(
                registerRequest.name(),
                registerRequest.email(),
                registerRequest.password(),
                registerRequest.role()
        );
        Member member = memberRepository.insert(request);
        return new MemberNameResponse(member.getName());
    }

    private void validateUserLogin(Member member, LoginRequest loginRequest) {
        if (!member.getEmail().equals(loginRequest.email()) ||
                !member.getPassword().equals(loginRequest.password())) {
            throw new InvalidMemberException("비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    public List<GetMemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(member -> new GetMemberResponse(member.id(), member.name()))
                .toList();
    }
}
