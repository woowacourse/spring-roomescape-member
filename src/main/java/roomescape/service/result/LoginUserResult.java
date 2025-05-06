package roomescape.service.result;

import roomescape.domain.User;

public record LoginUserResult(Long id, String name, String email, String password) {

    public static LoginUserResult from(final User user) {
        return new LoginUserResult(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}
