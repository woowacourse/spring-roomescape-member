package roomescape.exception.exception;

import lombok.Getter;

@Getter
public abstract class ForeignKeyConstraintException extends RuntimeException {
    public ForeignKeyConstraintException(String message) {
        super(message);
    }
}
