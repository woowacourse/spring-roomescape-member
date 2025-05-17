package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.admin.service.AdminService;
import roomescape.auth.dto.AdminLoginRequest;
import roomescape.auth.dto.MemberLoginRequest;
import roomescape.auth.infrastructure.Role;
import roomescape.auth.infrastructure.jwt.JwtPayload;
import roomescape.auth.infrastructure.jwt.JwtProvider;
import roomescape.common.globalexception.BadRequestException;
import roomescape.member.service.MemberService;

@Service
public class AuthService {

    private final MemberService memberService;
    private final AdminService adminService;
    private final JwtProvider jwtProvider;

    public AuthService(MemberService memberService, AdminService adminService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.adminService = adminService;
        this.jwtProvider = jwtProvider;
    }

    public String createMemberToken(MemberLoginRequest memberLoginRequest) {
        String email = memberLoginRequest.email();
        String password = memberLoginRequest.password();

        if (!memberService.existsByEmailAndPassword(email, password)) {
            throw new BadRequestException("이메일 혹은 비밀번호가 잘못되었습니다.");
        }

        return jwtProvider.createToken(new JwtPayload(email, Role.MEMBER));
    }

    public String createAdminToken(AdminLoginRequest adminLoginRequest) {
        String loginId = adminLoginRequest.loginId();
        String password = adminLoginRequest.password();

        if (!adminService.existsByLoginIdAndPassword(loginId, password)) {
            throw new BadRequestException("아이디 혹은 비밀번호가 잘못되었습니다.");
        }

        return jwtProvider.createToken(new JwtPayload(loginId, Role.ADMIN));
    }
}
