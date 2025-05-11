package roomescape.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.dao.MemberDao;

public class FakeMemberDaoImpl implements MemberDao {

    private final List<Member> members = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public long save(Member member) {
        members.add(member);
        return index.getAndIncrement();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.stream()
            .filter(member -> member.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return members.stream()
            .filter(
                member -> member.getEmail().equals(email) && member.getPassword().equals(password))
            .findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return members.stream()
            .filter(member -> member.getId().equals(id))
            .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return Collections.unmodifiableList(members);
    }
}
