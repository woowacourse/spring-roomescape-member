package roomescape.core.dto.user;

public class UserResponse {
    private String name;

    public UserResponse() {
    }

    public UserResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
