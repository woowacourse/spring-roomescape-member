package roomescape.exception.impl;

import roomescape.exception.RootException;

public class UserNameLengthExceedException extends RootException {

    private static final String MESSAGE = "이름은 10자를 넘어갈 수 없습니다.";

    public UserNameLengthExceedException() {
        super(MESSAGE);
    }
}
