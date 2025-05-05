package roomescape.unit.repository.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.member.Member;
import roomescape.repository.member.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    AtomicLong index = new AtomicLong(1L);
    List<Member> members = new ArrayList<>();

    @Override
    public long add(Member member) {
        long id = index.getAndIncrement();
        members.add(Member.of(id, member));
        return id;
    }

    @Override
    public boolean existsByUsernameAndPassword(String email, String password) {
        return members.stream()
                .anyMatch((member) -> member.isSamePassword(password) && member.isSameUsername(email));

    }

    @Override
    public Optional<Member> findById(long id) {
        return members.stream()
                .filter((member) -> member.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return members.stream()
                .filter(((member) -> member.isSamePassword(password) && member.isSameUsername(email)))
                .findAny();
    }

    @Override
    public boolean existByEmail(String email) {
        return members.stream()
                .anyMatch(((member) -> member.isSameUsername(email)));
    }
}
