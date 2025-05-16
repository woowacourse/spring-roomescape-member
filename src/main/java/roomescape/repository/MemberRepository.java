package roomescape.repository;

import java.util.List;
import roomescape.dto.MemberRequest;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
import roomescape.model.user.Role;

public interface MemberRepository {
    Member login(String username, String password);

    Name findNameByEmail(String email);

    List<Member> findAllUsers();

    Name findNameById(Long userId);

    Role findRoleByEmail(String email);

    Member findById(Long id);

    Member addMember(MemberRequest memberRequest);

    Member findByEmail(String userEmail);
}
