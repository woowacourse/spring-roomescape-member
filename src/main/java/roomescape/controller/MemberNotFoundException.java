package roomescape.controller;

import roomescape.exception.RoomescapeException;

//TODO 패키지 정리
public class MemberNotFoundException extends RoomescapeException {

    public MemberNotFoundException(final String message) {
        super(message);
    }
}
