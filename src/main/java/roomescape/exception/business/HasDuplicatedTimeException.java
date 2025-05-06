package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class HasDuplicatedTimeException extends RootBusinessException {

    private static final String MESSAGE = "중복된 시간입니다.";

    public HasDuplicatedTimeException() {
        super(MESSAGE);
    }
}
