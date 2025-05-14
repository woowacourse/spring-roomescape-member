package roomescape.exception.impl;

import roomescape.exception.RootException;

public class HasDuplicatedEmailException extends RootException {

    private static final String MESSAGE = "이미 해당 이메일이 존재합니다.";

    public HasDuplicatedEmailException() {
        super(MESSAGE);
    }
}
