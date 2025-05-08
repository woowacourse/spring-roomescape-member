package roomescape.service;

import jakarta.servlet.http.HttpSession;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberPassword;
import roomescape.repository.MemberRepository;
import roomescape.service.request.LoginRequest;

@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public LoginService(final PasswordEncoder passwordEncoder, final MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public void login(final LoginRequest request, final HttpSession session) {
        Member member = memberRepository.findByEmail(new MemberEmail(request.email()))
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버입니다."));
        MemberPassword rawPassword = new MemberPassword(request.password());
        if (!member.isMatchPassword(rawPassword, passwordEncoder)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        session.setAttribute("LOGIN_MEMBER", member);
    }
}
