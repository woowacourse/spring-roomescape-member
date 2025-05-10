package roomescape.exception.impl;

import roomescape.exception.RootException;

public class MemberNotFountException extends RootException {

    private static final String MESSAGE = "해당하는 사용자를 찾을 수 없습니다.";

    public MemberNotFountException() {
        super(MESSAGE);
    }
}
