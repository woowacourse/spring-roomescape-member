package roomescape.user.ui.dto;

import roomescape.user.application.dto.CreateUserServiceRequest;

public record CreateUserWebRequest(String name,
                                   String email,
                                   String password) {

    public CreateUserServiceRequest toServiceRequest() {
        return new CreateUserServiceRequest(name, email, password);
    }
}
