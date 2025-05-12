package roomescape.exceptions.member;

import roomescape.exceptions.EntityDuplicateException;

public class MemberDuplicateException extends EntityDuplicateException {

    public MemberDuplicateException(String message, String email) {
        super(message + " " + email);
    }
}

