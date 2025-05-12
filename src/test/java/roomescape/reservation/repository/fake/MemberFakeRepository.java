package roomescape.reservation.repository.fake;

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
    public Optional<Member> findByEmail(String email) {
        List<Member> emailMembers = members.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .toList();

        if (emailMembers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(emailMembers.getFirst());
    }

    public Long save(Member member) {
        Long generatedId = idGenerator.incrementAndGet();
        Member memberWithId = new Member(generatedId, member.getName(), member.getEmail(), member.getPassword(),
                member.getRole());
        members.put(generatedId, memberWithId);
        return generatedId;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> members = this.members.values().stream()
                .filter(member -> member.getId().equals(id))
                .toList();

        if (members.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(members.getFirst());
    }

    @Override
    public List<Member> findAll() {
        return members.values().stream()
                .toList();
    }

}
