package roomescape.user.ui.dto;

import roomescape.user.application.dto.CreateUserServiceRequest;

public record CreateUserWebRequest(String email,
                                   String password,
                                   String name) {

    public CreateUserServiceRequest toServiceRequest() {
        return new CreateUserServiceRequest(email, password, name);
    }
}
