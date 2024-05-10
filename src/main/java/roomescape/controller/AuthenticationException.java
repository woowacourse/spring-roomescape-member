package roomescape.controller;

import roomescape.exception.RoomescapeException;
//TODO 패키지 분리
public class AuthenticationException extends RoomescapeException {

    public AuthenticationException(final String message) {
        super(message);
    }
}
