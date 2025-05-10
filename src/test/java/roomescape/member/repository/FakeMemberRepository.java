package roomescape.member.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.member.domain.Member;

public class FakeMemberRepository implements MemberRepository {

    private final Map<Long, Member> members = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public boolean existsByEmailAndPassword(final String email, final String password) {
        return members.values()
                .stream()
                .anyMatch(member -> member.getEmail().equals(email) && member.getPassword().equals(password));
    }

    @Override
    public Optional<Member> findMemberByEmailAndPassword(final String email, final String password) {
        return members.values()
                .stream()
                .filter(member -> member.getEmail().equals(email) && member.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public boolean existsById(final Long id) {
        return members.containsKey(id);
    }

    @Override
    public Optional<Member> findUserById(final Long id) {
        return members.values()
                .stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    @Override
    public Member save(final Member member) {
        Long id = index.getAndIncrement();
        members.put(id, member);
        return Member.of(id, member.getName(), member.getEmail(), member.getPassword(),
                member.getMemberRole());
    }

    @Override
    public List<Member> findAllUsers() {
        return members.values().stream().toList();
    }
}
