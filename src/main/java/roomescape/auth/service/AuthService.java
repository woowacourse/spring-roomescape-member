package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.CreateTokenRequest;
import roomescape.common.exception.AuthorizationException;
import roomescape.member.model.Member;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.util.JwtTokenHelper;

@Service
public class AuthService {

    private final JdbcMemberRepository memberRepository;
    private final JwtTokenHelper jwtTokenHelper;

    public AuthService(JdbcMemberRepository memberRepository, JwtTokenHelper jwtTokenHelper) {
        this.memberRepository = memberRepository;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    public String createToken(CreateTokenRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(AuthorizationException::new);

        return jwtTokenHelper.createToken(member);
    }
}
