package roomescape.exception.impl;

import roomescape.exception.RootException;

public class PastDateException extends RootException {

    private static final String MESSAGE = "과거 날짜로 예약할 수 없습니다.";

    public PastDateException() {
        super(MESSAGE);
    }
}
