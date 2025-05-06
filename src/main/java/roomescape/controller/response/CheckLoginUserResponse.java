package roomescape.controller.response;

import roomescape.service.result.CheckLoginUserResult;

public record CheckLoginUserResponse(String name) {

    public static CheckLoginUserResponse from(CheckLoginUserResult checkLoginUserResult) {
        return new CheckLoginUserResponse(checkLoginUserResult.name());
    }
}
