package roomescape.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.entity.Member;
import roomescape.domain.repository.MemberRepository;

public class StubMemberRepository implements MemberRepository {

    private final List<Member> data = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong();

    public StubMemberRepository(Member... initialMembers) {
        data.addAll(List.of(initialMembers));
        long maxId = data.stream()
                .mapToLong(Member::getId)
                .max()
                .orElse(0L);
        idSequence.set(maxId);
    }

    @Override
    public Member save(Member member) {
        Member saved = new Member(idSequence.incrementAndGet(), member.getName(), member.getEmail(), member.getPassword(),
                member.getRole());
        data.add(saved);
        return saved;
    }

    @Override
    public boolean existsByEmail(String email) {
        return data.stream().anyMatch(m -> m.getEmail().equals(email));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return data.stream().filter(m -> m.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return data.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    @Override
    public List<Member> findAll() {
        return List.copyOf(data);
    }
}
