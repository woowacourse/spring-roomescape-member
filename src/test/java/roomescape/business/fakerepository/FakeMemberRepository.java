package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.Member;
import roomescape.persistence.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    private final List<Member> members = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(members);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Long add(Member member) {
        Member savedMember = new Member(
                idGenerator.getAndIncrement(),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole()
        );
        members.add(savedMember);
        return savedMember.getId();
    }
}
