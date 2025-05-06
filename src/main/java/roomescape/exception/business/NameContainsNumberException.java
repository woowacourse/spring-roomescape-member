package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class NameContainsNumberException extends RootBusinessException {

    private static final String MESSAGE = "이름에 숫자는 포함될 수 없습니다.";

    public NameContainsNumberException() {
        super(MESSAGE);
    }
}
