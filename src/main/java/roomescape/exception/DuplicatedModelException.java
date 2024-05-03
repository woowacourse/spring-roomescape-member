package roomescape.exception;

public class DuplicatedModelException extends RuntimeException {

    public DuplicatedModelException(Class<?> model) {
        super("이미 존재하는 " + model.getName() + " 은 추가할 수 없습니다.");
    }
}
