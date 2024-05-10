package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.user.Member;
import roomescape.exception.NotExistEmailException;
import roomescape.exception.NotExistException;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.input.MemberLoginInput;
import roomescape.service.dto.output.MemberCreateOutput;
import roomescape.service.dto.output.MemberLoginOutput;
import roomescape.service.dto.output.TokenLoginOutput;
import roomescape.service.util.TokenProvider;

import static roomescape.exception.ExceptionDomainType.MEMBER;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public MemberService(final MemberDao memberDao, final TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public MemberCreateOutput createMember(final MemberCreateInput memberCreateInput) {
        final Member member = memberDao.create(memberCreateInput.toMember());
        return MemberCreateOutput.toOutput(member);
    }

    public MemberLoginOutput loginMember(final MemberLoginInput memberLoginInput) {
        final Member member = memberDao.findByEmail(memberLoginInput.email())
                .orElseThrow(() -> new NotExistEmailException(memberLoginInput.email()));
        if (member.isNotEqualPassword(memberLoginInput.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return new MemberLoginOutput(tokenProvider.generateToken(member));
    }

    public TokenLoginOutput loginToken(final String token) {
        final long id = tokenProvider.decodeToken(token)
                .getId();
        final Member member = memberDao.findById(id)
                .orElseThrow(() -> new NotExistException(MEMBER, id));
        return TokenLoginOutput.toOutput(member);
    }
}
