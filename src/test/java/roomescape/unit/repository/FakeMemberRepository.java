package roomescape.unit.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Member;
import roomescape.repository.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    private final AtomicLong index = new AtomicLong(1L);
    private final List<Member> members = new ArrayList<>();

    @Override
    public List<Member> findAll() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public Optional<Member> findById(long id) {
        return members.stream()
                .filter(member -> member.getId() == id)
                .findAny();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return members.stream()
                .filter(member -> member.getEmail().equals(email)
                                  && member.getPassword().equals(password))
                .findAny();    }
}
