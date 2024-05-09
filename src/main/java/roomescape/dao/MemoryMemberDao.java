package roomescape.dao;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

@Repository
public class MemoryMemberDao implements MemberDao {

    private final Map<Long, Member> users = new ConcurrentHashMap<>();

    public MemoryMemberDao() {
        users.put(1L, new Member(1L, "user", "user@wooteco.com", "user1"));
    }

    @Override
    public Optional<Member> findById(Long id) {
        return users.values().stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return users.values().stream()
                .filter(member -> member.getEmail().equals(email) && member.getPassword().equals(password))
                .findFirst();
    }
}
