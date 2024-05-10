package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.LoginMemberRepository;
import roomescape.domain.LoginMember;
import roomescape.exception.AuthorizationException;
import roomescape.service.dto.LoginMemberResponse;
import roomescape.service.dto.TokenRequest;

@Service
public class LoginMemberService {
    private final LoginMemberRepository loginMemberRepository;

    public LoginMemberService(LoginMemberRepository loginMemberRepository) {
        this.loginMemberRepository = loginMemberRepository;
    }

    public void validateLogin(TokenRequest tokenRequest) {
        LoginMember loginMember = findByEmail(tokenRequest);
        validatePassword(tokenRequest, loginMember);
    }

    private LoginMember findByEmail(TokenRequest tokenRequest) {
        return loginMemberRepository.findByEmail(tokenRequest.email())
                .orElseThrow(() -> new AuthorizationException("잘못된 이메일입니다."));
    }

    private void validatePassword(TokenRequest tokenRequest, LoginMember loginMember) {
        if (!loginMember.getPassword().equals(tokenRequest.password())) {
            throw new AuthorizationException("잘못된 비밀번호입니다.");
        }
    }

    public LoginMemberResponse findByEmail(String email) {
        LoginMember loginMember = loginMemberRepository.findByEmail(email).get();
        return new LoginMemberResponse(
                loginMember.getId(), loginMember.getName(), loginMember.getEmail(), loginMember.getPassword(),
                loginMember.getRole());
    }
}
