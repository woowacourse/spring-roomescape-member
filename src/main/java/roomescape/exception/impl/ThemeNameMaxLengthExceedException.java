package roomescape.exception.impl;

import roomescape.exception.RootException;

public class ThemeNameMaxLengthExceedException extends RootException {

    private static final String MESSAGE = "테마 이름은 20자를 넘어갈 수 없습니다.";

    public ThemeNameMaxLengthExceedException() {
        super(MESSAGE);
    }
}
