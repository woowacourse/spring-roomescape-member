package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dao.MemberDao;
import roomescape.dto.auth.MemberInfoDto;
import roomescape.dto.auth.LoginRequestDto;
import roomescape.model.Member;
import roomescape.common.util.JwtProvider;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public AuthService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public String loginAndGenerateToken(LoginRequestDto loginRequestDto){
        Member member = loginRequestDto.convertToUser();
        boolean isExists = memberDao.existsByEmailAndPassword(member);
        if (!isExists) {
            throw new UnauthorizedException("존재하지 않는 email 혹은 틀린 password 입니다.");
        }
        return jwtProvider.createToken(member);
    }

    public MemberInfoDto findByToken(String token) {
        Long customerId = jwtProvider.getSubjectFromToken(token);
        Optional<Member> customer = memberDao.findById(customerId);
        if (customer.isEmpty()) {
            throw new NotFoundException("존재하지 않는 user 정보입니다.");
        }
        Member getMember = customer.get();
        return new MemberInfoDto(getMember.getId());
    }
}
