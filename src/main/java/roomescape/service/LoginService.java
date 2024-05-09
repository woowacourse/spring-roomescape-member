package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtProvider;
import roomescape.dao.MemberDao;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.RoomEscapeException;
import roomescape.mapper.MemberMapper;

@Service
public class LoginService {

    private final MemberMapper memberMapper = new MemberMapper();
    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public LoginService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequest loginRequest) {
        Long id = memberDao.findIdByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 사용자를 찾을 수 없습니다."));

        return jwtProvider.createAccessToken(id);
    }

    public MemberResponse checkMember(String token) {
        Long id = jwtProvider.parse(token);
        String name = memberDao.findNameById(id)
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 사용자를 찾을 수 없습니다."));

        return memberMapper.mapToResponse(name);
    }
}
