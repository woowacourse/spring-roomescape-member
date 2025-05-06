package roomescape.service.result;

import roomescape.domain.User;

public record RegisterUserResult(Long id, String email, String password, String name) {

    public static RegisterUserResult from(User user) {
        return new RegisterUserResult(user.getId(), user.getEmail(), user.getPassword(), user.getName());
    }
}
