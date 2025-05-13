package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dao.MemberDao;
import roomescape.controller.member.dto.MemberInfoDto;
import roomescape.controller.auth.dto.LoginRequestDto;
import roomescape.common.auth.JwtProvider;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public AuthService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public String loginAndGenerateToken(LoginRequestDto loginRequestDto) {
        return memberDao.findByEmailAndPassword(loginRequestDto.email(), loginRequestDto.password())
                .map(jwtProvider::createToken)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 email 혹은 틀린 password 입니다."));
    }

    public Optional<MemberInfoDto> findByToken(String token) {
        return Optional.ofNullable(jwtProvider.getSubjectFromToken(token))
                .flatMap(memberDao::findById)
                .map(member -> new MemberInfoDto(member.getId(), member.getRole()));
    }
}
