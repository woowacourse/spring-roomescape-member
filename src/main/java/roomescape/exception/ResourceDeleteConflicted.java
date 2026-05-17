package roomescape.exception;

import lombok.Getter;

@Getter
public class ResourceDeleteConflicted extends RuntimeException {

    private final String code;

    public ResourceDeleteConflicted(String message, String code) {
        super(message);
        this.code = code;
    }
}
