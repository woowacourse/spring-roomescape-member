package roomescape.exception;

public class NotExistEmailException extends IllegalArgumentException {
    public NotExistEmailException(final String email) {
        super(String.format("%s 해당하는 멤버가 없습니다", email));
    }
}
