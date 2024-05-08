package roomescape.dao;

import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO: DB를 이용하도록 수정
@Repository
public class MemberDao {

    private final List<Member> members;

    public MemberDao() {
        this.members = new ArrayList<>(){
            {
                add(new Member("name", "email@email.com", "password"));
            }
        };
    }

    public Member insert(String name, String email, String password) {
        Member newMember = new Member(name, email, password);
        members.add(newMember);
        return newMember;
    }

    public Optional<Member> findByEmail(String email) {
        return members.stream()
                .filter(user -> user.isEmailMatches(email))
                .findAny();
    }
}
