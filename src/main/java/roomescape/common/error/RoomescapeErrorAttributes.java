package roomescape.common.error;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Component
public class RoomescapeErrorAttributes extends DefaultErrorAttributes {

    private static final Logger log = LoggerFactory.getLogger(RoomescapeErrorAttributes.class);
    private static final String DEFAULT_ERROR_MESSAGE = "요청 처리 중 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.";

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> defaultAttributes = super.getErrorAttributes(webRequest, options);
        int status = (int) defaultAttributes.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        Throwable error = getError(webRequest);

        if (status >= HttpStatus.INTERNAL_SERVER_ERROR.value() && error != null) {
            logUnhandledException(webRequest, error);
        }

        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("status", status);
        attributes.put("message", toClientMessage(status));
        return attributes;
    }

    private String toClientMessage(int status) {
        if (status >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return DEFAULT_ERROR_MESSAGE;
        }
        return DEFAULT_ERROR_MESSAGE;
    }

    private void logUnhandledException(WebRequest webRequest, Throwable error) {
        if (webRequest instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest request = servletWebRequest.getRequest();
            log.error(
                    "Unhandled exception [{} {}]",
                    request.getMethod(),
                    request.getRequestURI(),
                    error
            );
            return;
        }
        log.error("Unhandled exception", error);
    }
}
