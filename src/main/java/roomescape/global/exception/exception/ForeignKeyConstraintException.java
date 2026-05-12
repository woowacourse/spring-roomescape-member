package roomescape.global.exception.exception;

import lombok.Getter;

@Getter
public class ForeignKeyConstraintException extends RuntimeException {
    public ForeignKeyConstraintException(String message) {
        super(message);
    }
}
