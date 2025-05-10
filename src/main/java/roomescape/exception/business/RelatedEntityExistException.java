package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class RelatedEntityExistException extends RootBusinessException {

    public RelatedEntityExistException(String message) {
        super(message);
    }
}
