package roomescape.member.exception;

import roomescape.global.exception.DuplicatedException;

public class MemberDuplicatedException extends DuplicatedException {

    public MemberDuplicatedException(final String message) {
        super(message);
    }
}
