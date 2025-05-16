package roomescape.test.fake;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Member;
import roomescape.repository.MemberRepository;

public class FakeH2MemberRepository implements MemberRepository {

    private final Map<Long, Member> members = new ConcurrentHashMap<>(
            Map.of(1L, new Member(1L, "아마", "email@email.com", "password", "member")));
    private final AtomicLong index = new AtomicLong(2);

    @Override
    public Optional<Member> findMemberByEmailAndPassword(final String email, final String password) {
        return members.values().stream()
                .filter(member -> Objects.equals(member.getEmail(), email) && Objects.equals(member.getPassword(),
                        password))
                .findFirst();
    }

    @Override
    public Optional<Member> findMemberById(final Long id) {
        return Optional.of(members.get(id));
    }

    @Override
    public List<Member> findAll() {
        return members.values().stream().toList();
    }

    @Override
    public long add(final Member member) {
        return 0;
    }
}
