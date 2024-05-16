package roomescape.reservation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;

public class FakeMemberDao implements MemberRepository {
    private final Map<Long, Member> members = new HashMap<>();

    @Override
    public Member save(final Member member) {
        long newId = members.size() + 1;
        Member savedMember = new Member(
                newId,
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                Role.MEMBER);
        members.put(newId, savedMember);
        return savedMember;
    }

    @Override
    public boolean existBy(String email, String password) {
        return members.values().stream()
                .anyMatch(member -> member.getEmail().equals(email) && member.getPassword().equals(password));
    }

    @Override
    public Member findByEmail(String email) {
        return members.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Member findById(long memberId) {
        if (members.containsKey(memberId)) {
            return members.get(memberId);
        }
        return null;
    }

    @Override
    public List<Member> findAll() {
        return members.values().stream().toList();
    }
}
