package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ForbiddenException extends RootException {

    private static final String MESSAGE = "권한이 없는 요청입니다.";

    public ForbiddenException() {
        super(MESSAGE);
    }
}
