package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.member.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.support.auth.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        final String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }

    @Transactional(readOnly = true)
    public MemberResponse findByToken(final String token) {
        final String email = jwtTokenProvider.getPayload(token);
        final Optional<Member> member = memberDao.findByEmail(email);
        if (member.isEmpty()) {
            throw new NoSuchElementException("멤버 정보를 찾을 수 없습니다.");
        }
        return MemberResponse.from(member.get());
    }

    @Transactional(readOnly = true)
    public Member findById(final Long id) {
        final Optional<Member> member = memberDao.findById(id);
        if (member.isEmpty()) {
            throw new NoSuchElementException("멤버 정보를 찾을 수 없습니다.");
        }
        return member.get();
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberDao.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
