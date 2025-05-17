package roomescape.util.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

public class MemberFakeRepository implements MemberRepository {
    private final Map<Long, Member> members = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return members.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .filter(member -> member.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public Long saveAndReturnId(Member memberWithoutId) {
        long id = idGenerator.incrementAndGet();
        Member member = memberWithoutId.createMemberWithId(id);
        members.put(id, member);

        return id;
    }

    @Override
    public List<Member> findAll() {
        return members.values().stream()
                .toList();
    }

    @Override
    public Optional<Member> findById(Long id) {
        if (members.containsKey(id)) {
            return Optional.of(members.get(id));
        }
        return Optional.empty();
    }
}
