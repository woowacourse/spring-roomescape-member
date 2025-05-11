package roomescape.repository;

import java.util.List;
import roomescape.model.user.Member;
import roomescape.model.user.Name;

public interface MemberRepository {
    Member login(String username, String password);

    Name findUserNameByUserEmail(String userEmail);

    List<Member> findAllUsers();

    Name findUserNameByUserId(Long userId);
}
