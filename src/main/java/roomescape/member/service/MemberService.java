package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Optional<Member> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean existsByEmailAndPassword(String email, String password) {
        return repository.existsByEmailAndPassword(email, password);
    }
}
