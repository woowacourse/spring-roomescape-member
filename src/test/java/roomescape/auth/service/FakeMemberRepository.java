package roomescape.auth.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

public class FakeMemberRepository implements MemberRepository {
    private final List<Member> members = new ArrayList<>();

    @Override
    public Member save(final Member member) {
        Member newMember = Member.of((long) members.size() + 1, member);
        members.add(newMember);
        return newMember;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return members.stream()
                .filter(member -> member.isSameEmail(email))
                .findFirst();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        return members.stream()
                .filter(member -> member.isSameId(id))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return Collections.unmodifiableList(members);
    }
}
