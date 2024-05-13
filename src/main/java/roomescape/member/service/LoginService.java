package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.global.auth.AuthUser;
import roomescape.global.exception.RoomEscapeException;
import roomescape.global.jwt.JwtProvider;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.ReservationMember;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.mapper.MemberMapper;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static roomescape.global.exception.ExceptionMessage.MEMBER_NOT_FOUND;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;
    private final MemberMapper memberMapper = new MemberMapper();

    public LoginService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequest loginRequest) {
        AuthUser authUser = memberDao.findIdByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new RoomEscapeException(NOT_FOUND, MEMBER_NOT_FOUND.getMessage()));

        return jwtProvider.createAccessToken(authUser);
    }

    public MemberResponse checkMember(AuthUser authUser) {
        ReservationMember member = memberDao.findById(authUser.getId());
        return memberMapper.mapToResponse(member);
    }
}
