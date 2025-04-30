package roomescape.exception.impl;

import roomescape.exception.RootException;

public class IdCannotBeNullException extends RootException {

    private static final String MESSAGE = "아이디는 null이 될 수 없습니다.";

    public IdCannotBeNullException() {
        super(MESSAGE);
    }
}
