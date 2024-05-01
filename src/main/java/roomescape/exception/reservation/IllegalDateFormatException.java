package roomescape.exception.reservation;

public class IllegalDateFormatException extends RuntimeException {

    public IllegalDateFormatException() {
        super("잘못된 날짜 형식을 입력하셨습니다.");
    }
}
