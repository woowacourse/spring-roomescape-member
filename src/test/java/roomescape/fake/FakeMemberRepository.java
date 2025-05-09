package roomescape.fake;

import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.persistence.query.CreateMemberQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {

    private Long id = 0L;
    List<Member> members = new ArrayList<>();

    @Override
    public Optional<Member> findByEmailAndPassword(final String email, final String password) {
        return members.stream()
                .filter(member -> Objects.equals(member.getEmail(), email))
                .filter(member -> Objects.equals(member.getPassword(), password))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        return members.stream()
                .filter(member -> Objects.equals(member.getId(), id))
                .findFirst();
    }

    @Override
    public Long create(final CreateMemberQuery createMemberQuery) {
        Member newMember = new Member(++id, createMemberQuery.name(), createMemberQuery.role(), createMemberQuery.email(), createMemberQuery.password());
        members.add(newMember);
        return id;
    }
}
