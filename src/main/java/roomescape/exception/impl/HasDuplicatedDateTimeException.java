package roomescape.exception.impl;

import roomescape.exception.RootException;

public class HasDuplicatedDateTimeException extends RootException {

    private static final String MESSAGE = "중복된 시간과 날짜입니다.";

    public HasDuplicatedDateTimeException() {
        super(MESSAGE);
    }
}

