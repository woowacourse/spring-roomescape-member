package roomescape.exception.impl;

import roomescape.exception.RootException;

public class NotAuthenticatedException extends RootException {

    private static final String MESSAGE = "인증되지 않은 사용자입니다.";

    public NotAuthenticatedException() {
        super(MESSAGE);
    }
}
