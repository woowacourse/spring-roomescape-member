package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberEmail;
import roomescape.business.domain.member.MemberName;
import roomescape.repository.MemberRepository;

public class FakeMemberDao implements MemberRepository {

    private final List<Member> members = new ArrayList<>();
    private int index = 0;

    @Override
    public Member save(final Member member) {
        Member savedMember = member.withId(++index);
        members.add(savedMember);
        return savedMember;
    }

    @Override
    public List<Member> findAll() {
        return members;
    }

    @Override
    public Optional<Member> findById(final long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(final MemberEmail email) {
        return members.stream()
                .anyMatch(member -> member.getMemberEmail().equals(email));
    }

    @Override
    public boolean existsByName(final MemberName name) {
        return members.stream()
                .anyMatch(member -> member.getMemberName().equals(name));
    }
}
