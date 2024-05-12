package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.response.MemberResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public Member createMember(Cookie[] cookies) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(cookies);
        return memberRepository.findById(memberId);
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
