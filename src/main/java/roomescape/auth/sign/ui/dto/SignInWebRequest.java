package roomescape.auth.sign.ui.dto;

import roomescape.auth.sign.application.dto.SignInServiceRequest;

public record SignInWebRequest(String email,
                               String password) {

    public SignInServiceRequest toServiceRequest() {
        return new SignInServiceRequest(email, password);
    }
}
