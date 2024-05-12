package roomescape.dto.response;

import roomescape.domain.Reservation;
import roomescape.domain.User;

public record UserLoginResponse(String name) {
    public static UserLoginResponse from(User user) {
        return new UserLoginResponse(
                user.getName()
        );
    }
}
