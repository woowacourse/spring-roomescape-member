package roomescape.user.dto;

import roomescape.user.model.User;

public class UserResponse {

    private final Long id;
    private final String name;

    private UserResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
