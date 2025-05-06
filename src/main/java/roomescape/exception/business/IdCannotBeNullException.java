package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class IdCannotBeNullException extends RootBusinessException {

    private static final String MESSAGE = "아이디는 null이 될 수 없습니다.";

    public IdCannotBeNullException() {
        super(MESSAGE);
    }
}
