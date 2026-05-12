package roomescape.theme.exception;

import roomescape.global.exception.exception.ForeignKeyConstraintException;

public class ThemeConstraintException extends ForeignKeyConstraintException {
    public ThemeConstraintException() {
        super(ThemeErrorCode.THEME_CONSTRAINT.getMessage());
    }
}
