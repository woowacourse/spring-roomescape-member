package roomescape.member.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    private final List<Member> members;

    private AtomicLong index = new AtomicLong(0);

    public FakeMemberRepository(List<Member> members) {
        this.members = members;
    }

    @Override
    public Long save(Member member) {
        long currentIndex = index.incrementAndGet();

        members.add(member.assignId(currentIndex));
        return currentIndex;
    }

    @Override
    public Member findById(Long id) {
        return members.stream()
                .filter(member -> Objects.equals(member.getId(), id))
                .findAny()
                .orElseThrow();
    }

    @Override
    public boolean existByEmail(String email) {
        return members.stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }
}
