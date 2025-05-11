package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.common.auth.JwtTokenProvider;
import roomescape.common.exception.auth.InvalidAuthException;
import roomescape.common.exception.member.MemberException;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;
import roomescape.dto.auth.request.LoginRequest;
import roomescape.dto.auth.response.MemberNameResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(final JwtTokenProvider jwtTokenProvider,
                       final MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String createToken(final LoginRequest request) {
        final Member member = findMemberByEmail(request.email());
        member.validatePassword(request.password());
        return jwtTokenProvider.createToken(member);
    }

    private Member findMemberByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberException("해당 이메일의 회원이 존재하지 않습니다."));
    }

    public MemberNameResponse checkLogin(final String token) {
        final Member member = findMemberByToken(token);
        return MemberNameResponse.from(member);
    }

    public LoginMember findLoginMemberByToken(final String token) {
        final Member member = findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    private Member findMemberByToken(final String token) {
        final Long id = jwtTokenProvider.getSubjectFromPayloadBy(token);
        return findMemberById(id);
    }

    private Member findMemberById(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new InvalidAuthException("회원 정보를 찾을 수 없습니다. 회원가입 또는 로그인을 해주세요"));
    }

}
