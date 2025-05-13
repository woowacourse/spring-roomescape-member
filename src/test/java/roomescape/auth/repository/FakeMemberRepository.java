package roomescape.auth.repository;

import roomescape.auth.entity.Member;
import roomescape.global.exception.conflict.MemberEmailConflictException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {
    private final List<Member> members = new ArrayList<>();

    @Override
    public List<Member> findAll() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public Optional<Member> findById(Long userId) {
        return members.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return members.stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Member save(Member member) {
        if (findByEmail(member.getEmail()).isPresent()) {
            throw new MemberEmailConflictException();
        }
        if (member.getId() == null) {
            final long nextId = members.stream()
                    .mapToLong(Member::getId)
                    .max()
                    .orElse(0) + 1;
            member = new Member(
                    nextId,
                    member.getName(),
                    member.getRole().name(),
                    member.getEmail(),
                    member.getPassword()
            );
        }
        members.add(member);
        return member;
    }
}
