package roomescape.exception;

import java.util.List;

// TODO: 에러메시지 클래스라면 '에러'의 의미를 담는 클래스명
// TODO: dto 를 붙이는 것에 대한 장단점 따져보기?
public record ErrorMessageResponse(
        List<String> messages
) {
    public ErrorMessageResponse(String message) {
        this(List.of(message));
    }
}
