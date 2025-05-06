package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class UserNameLengthExceedException extends RootBusinessException {

    private static final String MESSAGE = "이름은 10자를 넘어갈 수 없습니다.";

    public UserNameLengthExceedException() {
        super(MESSAGE);
    }
}
