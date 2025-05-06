package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class DuplicatedEmailException extends RootBusinessException {

    private static final String MESSAGE = "중복된 이메일입니다.";

    public DuplicatedEmailException() {
        super(MESSAGE);
    }
}
