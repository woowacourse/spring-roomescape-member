package roomescape.member.application.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.global.jwt.TokenProvider;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.MemberResponse;
import roomescape.member.presentation.dto.SignUpRequest;
import roomescape.member.presentation.dto.SignUpResponse;
import roomescape.member.presentation.dto.TokenRequest;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("유저 정보를 찾을 수 없습니다."));

        return tokenProvider.createToken(member);
    }

    public MemberResponse findByToken(String token) {
        Long id = tokenProvider.getInfo(token);
        return findById(id);
    }

    private MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저 정보를 찾을 수 없습니다."));

        return new MemberResponse(member);
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        return new SignUpResponse(memberRepository.insert(signUpRequest).getId());
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAllMembers().stream()
                .map(MemberResponse::new)
                .toList();
    }
}
