package roomescape.dao;

import org.springframework.stereotype.Repository;
import roomescape.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO: DB를 이용하도록 수정
@Repository
public class LoginDao {

    private final List<User> users;

    public LoginDao() {
        this.users = new ArrayList<>();
    }

    public User insert(String name, String email, String password) {
        User newUser = new User(name, email, password);
        users.add(newUser);
        return newUser;
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.isEmailMatches(email))
                .findAny();
    }
}
