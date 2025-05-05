package roomescape.exception.impl;

import roomescape.exception.RootException;

public class UserNotFoundException extends RootException {

    private static final String MESSAGE = "해당하는 유저가 존재하지 않습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
