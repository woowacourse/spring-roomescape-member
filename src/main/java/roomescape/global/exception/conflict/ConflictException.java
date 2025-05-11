package roomescape.global.exception.conflict;

public class ConflictException extends IllegalArgumentException {
    public ConflictException(String resource) {
        super(String.format("중복되는 %s이(가) 이미 존재합니다.", resource));
    }
}
