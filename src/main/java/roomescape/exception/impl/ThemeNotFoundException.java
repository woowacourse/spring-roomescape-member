package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ThemeNotFoundException extends RootException {

    private static final String MESSAGE = "해당하는 테마가 존재하지 않습니다.";

    public ThemeNotFoundException() {
        super(MESSAGE);
    }
}
