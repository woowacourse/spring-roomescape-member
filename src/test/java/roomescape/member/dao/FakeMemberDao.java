package roomescape.member.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.member.Member;

public class FakeMemberDao implements MemberDao {

    private final List<Member> fakeMembers = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeMemberDao(Member... members) {
        Arrays.stream(members).forEach(member -> fakeMembers.add(member));
    }

    @Override
    public Optional<Member> findMember(String payload) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }
}
