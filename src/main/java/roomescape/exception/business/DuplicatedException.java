package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class DuplicatedException extends RootBusinessException {
    
    public DuplicatedException(String message) {
        super(message);
    }
}
