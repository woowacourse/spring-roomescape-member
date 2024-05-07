package roomescape.reservation.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

public class FakeMemberDao implements MemberRepository {

    private final Map<Long, Member> members = new HashMap<>();

    @Override
    public Member save(final Member member) {
        members.put((long) members.size() + 1, member);
        return member;
    }

    @Override
    public Optional<Member> findBy(String email) {
        return members.values().stream()
                .filter(member -> Objects.equals(member.getEmail(), email))
                .findFirst();
    }

    @Override
    public boolean existsBy(String email, String password) {
        return true;
    }
}
