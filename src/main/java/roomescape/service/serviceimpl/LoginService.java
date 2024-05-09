package roomescape.service.serviceimpl;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.infrastructure.auth.JwtTokenGenerator;
import roomescape.infrastructure.auth.Token;

@Service
public class LoginService {
    private final JwtTokenGenerator tokenGenerator;

    public LoginService(JwtTokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    public Token login(Member member, String email, String password) {
        validateLogin(member, email, password);
        return tokenGenerator.generate(member);
    }

    public Long findMemberIdByToken(Token token) {
        return tokenGenerator.getMemberId(token);

    }

    private void validateLogin(Member member, String email, String password) {
        if (password.equals(member.getPassword())) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 아이디 혹은 비밀번호가 일치 하지 않습니다.");
    }


}
