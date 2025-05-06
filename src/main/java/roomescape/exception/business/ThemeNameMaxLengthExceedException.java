package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ThemeNameMaxLengthExceedException extends RootBusinessException {

    private static final String MESSAGE = "테마 이름은 20자를 넘어갈 수 없습니다.";

    public ThemeNameMaxLengthExceedException() {
        super(MESSAGE);
    }
}
