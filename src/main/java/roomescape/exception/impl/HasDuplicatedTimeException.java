package roomescape.exception.impl;

import roomescape.exception.RootException;

public class HasDuplicatedTimeException extends RootException {

    private static final String MESSAGE = "중복된 시간입니다.";

    public HasDuplicatedTimeException() {
        super(MESSAGE);
    }
}

