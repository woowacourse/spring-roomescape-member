package roomescape.exception;

import java.time.LocalTime;

public class DuplicatePlayTimeException extends IllegalStateException {

    private static final String DEFAULT_MESSAGE = "해당 시간에 이미 존재하는 startAt이 있습니다. startAt : ";

    public DuplicatePlayTimeException(final String message) {
        super(message);
    }

    public DuplicatePlayTimeException(final LocalTime startAt) {
        super(DEFAULT_MESSAGE + startAt);
    }
}
