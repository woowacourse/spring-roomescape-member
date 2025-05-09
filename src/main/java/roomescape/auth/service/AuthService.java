package roomescape.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.entity.Member;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.repository.JdbcAuthRepository;
import roomescape.exception.impl.HasDuplicatedEmailException;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JdbcAuthRepository jdbcAuthRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final JdbcAuthRepository jdbcAuthRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jdbcAuthRepository = jdbcAuthRepository;
    }

    @Transactional
    public Member register(
            final String name,
            final String email,
            final String password
    ) {
        if (jdbcAuthRepository.isExistEmail(email)) {
            throw new HasDuplicatedEmailException();
        }
        Member member = Member.beforeSave(name, email, password);
        return jdbcAuthRepository.save(member);
    }

    public String login(final String email, final String password) {
        return jwtTokenProvider.createToken(email);
    }
}
