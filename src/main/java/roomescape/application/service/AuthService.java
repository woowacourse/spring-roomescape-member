package roomescape.application.service;

import org.springframework.stereotype.Service;
import roomescape.application.util.PasswordUtil;
import roomescape.domain.exception.MemberDuplicatedException;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.request.SignupRequest;
import roomescape.presentation.dto.response.LoginResponse;
import roomescape.presentation.dto.response.SignupResponse;

@Service
public class AuthService {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    public AuthService(final TokenService tokenService, final MemberRepository memberRepository) {
        this.tokenService = tokenService;
        this.memberRepository = memberRepository;
    }

    public SignupResponse signup(final SignupRequest request, final boolean isAdmin) {
        if (memberRepository.existByEmail(request.email())) {
            throw new MemberDuplicatedException();
        }

        String encodedPassword = PasswordUtil.encrypt(request.password());

        String role = "user";
        if (isAdmin) {
            role = "admin";
        }

        Member member = new Member(request.name(), request.email(), encodedPassword, role);
        Long memberId = memberRepository.save(member);
        return new SignupResponse(memberId, member.getName(), member.getEmail());
    }

    public LoginResponse login(final LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email());
        if (!PasswordUtil.matches(request.password(), member.getPassword())) {
            throw new UnauthorizedException();
        }

        String token = tokenService.createToken(member);
        return new LoginResponse(token);
    }

    public Member loadMemberByAuthInformation(final String token) {
        Long memberId = Long.valueOf(tokenService.checkByToken(token));
        return memberRepository.findById(memberId);
    }
}
