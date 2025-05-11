package roomescape.repository;

import java.util.List;
import roomescape.model.user.User;
import roomescape.model.user.UserName;

public interface MemberRepository {
    User login(String username, String password);

    UserName findUserNameByUserEmail(String userEmail);

    List<User> findAllUsers();

    UserName findUserNameByUserId(Long userId);
}
