package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class PastDateException extends RootBusinessException {

    private static final String MESSAGE = "과거 날짜로 예약할 수 없습니다.";

    public PastDateException() {
        super(MESSAGE);
    }
}
