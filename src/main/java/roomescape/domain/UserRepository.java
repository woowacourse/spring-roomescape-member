package roomescape.domain;

public interface UserRepository {
    User save(User user);

    User findById(Long id);
}
