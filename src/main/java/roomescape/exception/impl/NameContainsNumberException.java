package roomescape.exception.impl;

import roomescape.exception.RootException;

public class NameContainsNumberException extends RootException {

    private static final String MESSAGE = "이름에 숫자는 포함될 수 없습니다.";

    public NameContainsNumberException() {
        super(MESSAGE);
    }
}
