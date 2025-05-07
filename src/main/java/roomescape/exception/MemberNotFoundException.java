package roomescape.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("존재하지 않는 멤버 id입니다.");
    }
}
