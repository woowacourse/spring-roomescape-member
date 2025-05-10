package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class NotFoundException extends RootBusinessException {
    
    public NotFoundException(String message) {
        super(message);
    }
}
