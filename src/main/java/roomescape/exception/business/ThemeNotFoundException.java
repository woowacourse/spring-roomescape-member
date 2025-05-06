package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class ThemeNotFoundException extends RootBusinessException {

    private static final String MESSAGE = "해당하는 테마가 존재하지 않습니다.";

    public ThemeNotFoundException() {
        super(MESSAGE);
    }
}
