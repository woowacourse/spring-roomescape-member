package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.AuthorizationException;
import roomescape.common.exception.InvalidEmailException;
import roomescape.common.exception.InvalidIdException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignupRequest;
import roomescape.member.dto.MemberTokenResponse;
import roomescape.member.login.authorization.JwtTokenProvider;

@Service
public class MemberService {
    private static final String INVALID_MEMBER_ID_EXCEPTION_MESSAGE = "해당 멤버 아이디는 존재하지 않습니다";
    private static final String INVALID_MEMBER_EMAIL_EXCEPTION_MESSAGE = "해당 멤버 이메일은 존재하지 않습니다";
    private static final String AUTHENTICATION_FAIL_EXCEPTION_MESSAGE = "회원 로그인에 실패했습니다";
    public static final String DUPLICATE_MEMBER_EXCEPTION_MESSAGE = "이미 존재하는 회원입니다";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        return memberDao.findAll().stream()
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getPassword())
                )
                .toList();
    }

    public MemberResponse findById(Long id) {
        Member member = memberDao.findById(id)
                .orElseThrow(() -> new InvalidIdException(INVALID_MEMBER_ID_EXCEPTION_MESSAGE));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public MemberResponse findByEmail(final String email) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException(INVALID_MEMBER_EMAIL_EXCEPTION_MESSAGE));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public MemberResponse findByToken(final String token) {
        String email = jwtTokenProvider.getPayload(token);
        return findByEmail(email);
    }

    public MemberTokenResponse createToken(final MemberLoginRequest memberLoginRequest) {
        MemberResponse memberResponse = findByEmail(memberLoginRequest.email());
        validatePassword(memberLoginRequest.email(), memberResponse.password());

        String accessToken = jwtTokenProvider.createToken(memberLoginRequest.email());
        return new MemberTokenResponse(accessToken);
    }

    public MemberResponse add(final MemberSignupRequest memberSignupRequest) {
        if (memberDao.existsByEmail(memberSignupRequest.email())) {
            throw new AuthorizationException(DUPLICATE_MEMBER_EXCEPTION_MESSAGE);
        }
        Member member = new Member(
                memberSignupRequest.name(),
                memberSignupRequest.email(),
                memberSignupRequest.password()
        );
        Member savedMember = memberDao.add(member);

        return new MemberResponse(
                savedMember.getId(),
                savedMember.getName(),
                savedMember.getEmail(),
                savedMember.getPassword()
        );
    }

    private void validatePassword(String email, String password) {
        if (!memberDao.isPasswordMatch(email, password)) {
            throw new AuthenticationException(AUTHENTICATION_FAIL_EXCEPTION_MESSAGE);
        }
    }
}
