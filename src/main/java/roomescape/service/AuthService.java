package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.dao.MemberDao;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberNameResponse;
import roomescape.exception.InvalidAuthException;
import roomescape.exception.MemberException;

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
        Member member = findMemberByEmail(request.email());
        member.validatePassword(request.password());
        return jwtTokenProvider.createToken(member);
    }

    private Member findMemberByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberException("해당 이메일의 회원이 존재하지 않습니다."));
    }

    public MemberNameResponse checkLogin(final String token) {
        Member member = findMemberByToken(token);
        return MemberNameResponse.from(member);
    }

    public LoginMember findLoginMemberByToken(final String token) {
        Member member = findMemberByToken(token);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    private Member findMemberByToken(final String token) {
        Long id = jwtTokenProvider.getSubjectFromPayloadBy(token);
        return findMemberById(id);
    }

    private Member findMemberById(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new InvalidAuthException("회원 정보를 찾을 수 없습니다."));
    }

}
