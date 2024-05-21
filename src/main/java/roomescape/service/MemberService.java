package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LoginMember;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.infrastructure.TokenGenerator;
import roomescape.repository.MemberDao;

import java.util.List;

@Service
public class MemberService {
    private final TokenGenerator tokenGenerator;
    private final MemberDao memberDao;

    public MemberService(final TokenGenerator tokenGenerator, final MemberDao memberDao) {
        this.tokenGenerator = tokenGenerator;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.email(), tokenRequest.password());
        String accessToken = tokenGenerator.createToken(tokenRequest.email(), member.getRole().name());
        return TokenResponse.from(accessToken);
    }

    public MemberResponse findMemberByToken(final String token) {
        String email = tokenGenerator.getPayload(token);
        Member member = memberDao.findByEmail(email);
        return MemberResponse.from(member);
    }

    public LoginMember findLoginMemberByToken(final String token) {
        String email = tokenGenerator.getPayload(token);
        Member member = memberDao.findByEmail(email);
        return LoginMember.from(member);
    }

    public List<MemberResponse> findAll() {
        return memberDao.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
