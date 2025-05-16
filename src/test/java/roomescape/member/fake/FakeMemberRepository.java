package roomescape.member.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.member.domain.Member;
import roomescape.member.domain.Password;
import roomescape.member.repository.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    private final List<Member> data = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong();

    public FakeMemberRepository(final Member... members) {
        data.addAll(List.of(members));
        long maxId = data.stream()
                .mapToLong(Member::getId)
                .max()
                .orElse(0L);
        atomicLong.set(maxId);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return data.stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }

    @Override
    public Optional<Member> findById(final Long id) {
        return data.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return data.stream()
                .filter(m -> m.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        return data.stream()
                .filter(m -> m.getEmail().equals(email) && m.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public void save(final Member inputMember) {
        final Long newId = atomicLong.incrementAndGet();
        final Member member = Member.builder().id(newId)
                .name(inputMember.getName())
                .email(inputMember.getEmail())
                .password(Password.createForMember(inputMember.getPassword()))
                .role(inputMember.getRole())
                .build();
        data.add(member);
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(data);
    }
}
