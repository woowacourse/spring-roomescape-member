package roomescape.exception.business;

import roomescape.exception.RootBusinessException;

public class InvalidCreateArgumentException extends RootBusinessException {
    
    public InvalidCreateArgumentException(String message) {
        super(message);
    }
}
