package roomescape.repository;

import roomescape.model.user.User;
import roomescape.model.user.UserName;

public interface LoginRepository {
    User login(String username, String password);

    UserName findUserNameByUserEmail(String userEmail);
}
