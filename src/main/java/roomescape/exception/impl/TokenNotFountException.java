package roomescape.exception.impl;

import roomescape.exception.RootException;

public class TokenNotFountException extends RootException {

    private static final String MESSAGE = "토큰을 찾을 수 없습니다.";

    public TokenNotFountException() {
        super(MESSAGE);
    }
}
