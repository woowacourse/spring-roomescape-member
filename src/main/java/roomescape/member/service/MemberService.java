package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.AuthorizationException;
import roomescape.common.exception.InvalidEmailException;
import roomescape.common.exception.InvalidIdException;
import roomescape.common.exception.message.IdExceptionMessage;
import roomescape.common.exception.message.LoginExceptionMessage;
import roomescape.common.exception.message.MemberExceptionMessage;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignupRequest;
import roomescape.member.dto.MemberTokenResponse;
import roomescape.member.login.authorization.AuthorizationHandler;
import roomescape.member.login.authorization.JwtTokenProvider;
import roomescape.member.login.authorization.TokenAuthorizationHandler;

@Service
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationHandler<String> authorizationHandler;
    private final MemberDao memberDao;

    public MemberService(
            JwtTokenProvider jwtTokenProvider,
            TokenAuthorizationHandler tokenAuthorizationHandler,
            MemberDao memberDao
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationHandler = tokenAuthorizationHandler;
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        return memberDao.findAll().stream()
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getName(),
                        member.getEmail())
                )
                .toList();
    }

    public MemberResponse findById(Long id) {
        Member member = memberDao.findById(id)
                .orElseThrow(() -> new InvalidIdException(IdExceptionMessage.INVALID_MEMBER_ID.getMessage()));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findByEmail(final String email) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException(MemberExceptionMessage.INVALID_MEMBER_EMAIL.getMessage()));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public MemberResponse findByToken(final String token) {
        String email = jwtTokenProvider.getPayloadEmail(token);
        return findByEmail(email);
    }

    public MemberTokenResponse createToken(final MemberLoginRequest memberLoginRequest) {
        Member member = memberDao.findByEmail(memberLoginRequest.email()).get();
        validatePassword(memberLoginRequest.email(), member.getPassword());
        String role = assignRole(memberLoginRequest.email()).getRole();
        String accessToken = jwtTokenProvider.createToken(memberLoginRequest.email(), role);
        return new MemberTokenResponse(accessToken);
    }

    public MemberResponse add(final MemberSignupRequest memberSignupRequest) {
        if (memberDao.existsByEmail(memberSignupRequest.email())) {
            throw new AuthorizationException(MemberExceptionMessage.DUPLICATE_MEMBER.getMessage());
        }
        Member member = MemberSignupRequest.from(memberSignupRequest);
        Member savedMember = memberDao.add(member);

        return new MemberResponse(
                savedMember.getId(),
                savedMember.getName(),
                savedMember.getEmail()
        );
    }

    private Role assignRole(String email) {
        Member member = memberDao.findByEmail(email).get();
        return member.getRole();
    }

    private void validatePassword(String email, String password) {
        Member member = memberDao.findByEmail(email).get();
        if (!password.equals(member.getPassword())) {
            throw new AuthenticationException(LoginExceptionMessage.AUTHENTICATION_FAIL.getMessage());
        }
    }
}
