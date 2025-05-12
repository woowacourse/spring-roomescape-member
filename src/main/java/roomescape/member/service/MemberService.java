package roomescape.member.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.globalexception.BadRequestException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member findExistingMemberByEmailAndPassword(String email, String password) {
        return repository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new BadRequestException("이메일 혹은 비밀번호가 잘못되었습니다."));
    }

    public Optional<Member> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean existsByEmailAndPassword(String email, String password) {
        return repository.existsByEmailAndPassword(email, password);
    }
}
