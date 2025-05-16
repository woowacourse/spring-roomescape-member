package roomescape.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    public MemberService(final MemberRepository memberRepository, final AuthService authService) {
        this.memberRepository = memberRepository;
        this.authService = authService;
    }

    public List<MemberResponse> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }

    public Member findMemberWithEmailAndPassword(LoginRequest request) {
        Optional<Member> member = memberRepository.findMemberByEmailAndPassword(request.email(), request.password());
        return member.orElseThrow(() -> new NotFoundException("[ERROR] 사용자 정보를 가져오지 못했습니다."));
    }

    public Member findMemberById(Long id) {
        Optional<Member> member = memberRepository.findMemberById(id);
        return member.orElseThrow(() -> new NotFoundException("[ERROR] 사용자 정보를 가져오지 못했습니다."));
    }

    public LoginCheckResponse checkMember(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return new LoginCheckResponse(authService.getClaimsFromCookie(cookies).get("name", String.class));
    }
}
