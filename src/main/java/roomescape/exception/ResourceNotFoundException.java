package roomescape.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RoomescapeBaseException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(HttpStatus.NOT_FOUND, resourceName + "을(를) 찾을 수 없습니다. id=" + id);
    }
}