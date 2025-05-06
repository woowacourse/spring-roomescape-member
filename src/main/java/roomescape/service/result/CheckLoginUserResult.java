package roomescape.service.result;

import roomescape.domain.User;

public record CheckLoginUserResult(String name) {

    public static CheckLoginUserResult from(User user) { //TODO: from으로 할지, of로 할지
        return new CheckLoginUserResult(user.getName());
    }
}
