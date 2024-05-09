package roomescape.service.serviceimpl;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.response.MemberResponse;
import roomescape.infrastructure.auth.JwtTokenGenerator;
import roomescape.infrastructure.auth.Token;
import roomescape.repository.MemberDao;

@Service
public class LoginService {
    private final MemberDao memberDao;
    private final JwtTokenGenerator tokenGenerator;

    public LoginService(MemberDao memberDao, JwtTokenGenerator tokenGenerator) {
        this.memberDao = memberDao;
        this.tokenGenerator = tokenGenerator;
    }

    public Token login(String email, String password) {
        validateLogin(email, password);
        return tokenGenerator.generate(findMemberByEmail(email));
    }

    public MemberResponse checkMember(Token token) {
        Member member = findMemberById(tokenGenerator.getMemberId(token));
        return new MemberResponse(member);
    }

    private Member findMemberById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] 멤버 id는 null일 수 없습니다.");
        }
        return memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 멤버를 찾을 수 없습니다."));
    }

    private void validateLogin(String email, String password) {
        Member member = findMemberByEmail(email);
        if (password.equals(member.getPassword())) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 아이디 혹은 비밀번호가 일치 하지 않습니다.");
    }

    private Member findMemberByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] " + email + " 을 가진 회원이 존재하지 않습니다."));
    }
}
