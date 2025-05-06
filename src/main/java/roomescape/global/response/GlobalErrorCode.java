package roomescape.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    NO_ELEMENTS("GF001"),
    WRONG_ARGUMENT("GF002");

    private final String value;
}
