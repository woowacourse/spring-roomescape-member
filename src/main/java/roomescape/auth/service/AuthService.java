package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.repository.AuthRepository;
import roomescape.entity.Member;
import roomescape.exception.impl.HasDuplicatedEmailException;
import roomescape.exception.impl.MemberNotFountException;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final AuthRepository authRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authRepository = authRepository;
    }

    public Member register(
            final String name,
            final String email,
            final String password
    ) {
        if (authRepository.isExistEmail(email)) {
            throw new HasDuplicatedEmailException();
        }
        Member member = Member.beforeMemberSave(name, email, password);
        return authRepository.save(member);
    }

    public String login(final String email, final String password) {
        Member member = authRepository.findByEmail(email);
        if (member == null) {
            throw new MemberNotFountException();
        }
        member.validatePassword(password);
        return jwtTokenProvider.createToken(member);
    }


    public Member findMemberByToken(final String token) {
        long id = jwtTokenProvider.extractMemberId(token);
        Member member = authRepository.findMemberById(id);
        if (member == null) {
            throw new MemberNotFountException();
        }
        return member;
    }
}
