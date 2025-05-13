package roomescape.member.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import roomescape.member.Member;

public class FakeMemberDao implements MemberDao {

    private final List<Member> fakeMembers = new ArrayList<>();

    public FakeMemberDao(Member... members) {
        Arrays.stream(members).forEach(member -> fakeMembers.add(member));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(fakeMembers);
    }

    @Override
    public Optional<Member> findMember(String email) {
        return fakeMembers.stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return fakeMembers.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }
}
