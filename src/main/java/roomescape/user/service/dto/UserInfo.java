package roomescape.user.service.dto;

import roomescape.user.domain.User;

public record UserInfo(long id, String name, String email, String password) {
    public UserInfo(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}
