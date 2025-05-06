package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class UserNotFoundException extends RootBusinessException {

    private static final String MESSAGE = "해당하는 유저가 존재하지 않습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
