package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.service.dto.LoginInfo;
import roomescape.member.service.dto.TokenResponse;
import roomescape.member.service.dto.MemberInfo;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public AuthService(final MemberDao memberDao, final TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse tokenLogin(final LoginInfo loginInfo) {
        final Member loginMember = memberDao.findByEmailAndPassword(loginInfo.email(), loginInfo.password())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 혹은 비밀번호입니다."));
        Long memberId = loginMember.getId();
        String token = tokenProvider.createToken(memberId.toString());
        return new TokenResponse(token);
    }

    public MemberInfo getMemberInfoByToken(String token) {
        long memberId = Long.parseLong(tokenProvider.parsePayload(token));
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 정보입니다."));
        return new MemberInfo(member);
    }
}
