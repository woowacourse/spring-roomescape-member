package roomescape.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.User;
import roomescape.repository.dao.UserDao;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserDao userDao;

    public User save(User user) {
        return userDao.insertAndGet(user);
    }

    public Optional<User> findById(Long id) {
        return userDao.selectById(id);
    }

    public User getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    public Optional<User> findByEmail(String email) {
        return userDao.selectByEmail(email);
    }

    // TODO: id or pw not found 시 커스텀 익셉션 반환하도록 변경
    public User getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 또는 패스워드입니다."));
    }
}
