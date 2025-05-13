package roomescape.fake;

import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.member.repository.MemberRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MemberFakeRepository implements MemberRepository {
    private final Map<Long, Member> members = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public MemberFakeRepository() {
        long memberId1 = idGenerator.getAndIncrement();
        Member defaultMember1 = new Member(memberId1, "abcd1@email.com", "12345", "회원1", Role.USER);
        members.put(memberId1, defaultMember1);

        long memberId2 = idGenerator.getAndIncrement();

        Member defaultMember2 = new Member(memberId2, "abcd2@email.com", "123456", "회원2", Role.USER);
        members.put(memberId2, defaultMember2);
    }

    @Override
    public Member save(Member member) {
        long newId = idGenerator.getAndIncrement();
        Member savedMember = new Member(newId, member.getEmail(), member.getPassword(), member.getName(), member.getRole());
        members.put(newId, savedMember);
        return savedMember;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public List<Member> findAll() {
        return members.values().stream()
                .toList();
    }
}
