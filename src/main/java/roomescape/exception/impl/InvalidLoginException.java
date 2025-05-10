package roomescape.exception.impl;

import roomescape.exception.RootException;

public class InvalidLoginException extends RootException {

    private static final String MESSAGE = "잘못된 로그인 정보입니다.";

    public InvalidLoginException() {
        super(MESSAGE);
    }
}
