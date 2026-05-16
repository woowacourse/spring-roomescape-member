package roomescape.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class HttpStatusMapper {
    public static final Map<Class<? extends RuntimeException>, HttpStatus> STATUS_MAP = Map.of(
            ConflictException.class, HttpStatus.CONFLICT,
            InvalidRequestValueException.class, HttpStatus.BAD_REQUEST,
            NotFoundResourceException.class, HttpStatus.NOT_FOUND,
            UnauthorizedException.class, HttpStatus.UNAUTHORIZED
    );
}
