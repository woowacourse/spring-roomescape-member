package roomescape.web.api.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Map;

public abstract class CustomAuthInterceptor implements HandlerInterceptor {
    private final Map<String, HttpMethod[]> includedPathPatterns;

    public CustomAuthInterceptor(Map<String, HttpMethod[]> includedPathPatterns) {
        this.includedPathPatterns = includedPathPatterns;
    }

    public CustomAuthInterceptor addPathPatterns(String requestURI, HttpMethod... method) {
        includedPathPatterns.put(requestURI, method);
        return this;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();

        if (isUnsupportedURI(uri)) {
            return true;
        }
        if (isUnsupportedMethod(uri, request.getMethod())) {
            return true;
        }

        return handle(request);
    }

    private boolean isUnsupportedURI(String requestURI) {
        return !includedPathPatterns.containsKey(requestURI);
    }

    private boolean isUnsupportedMethod(String requestURI, String requestMethod) {
        HttpMethod method = HttpMethod.valueOf(requestMethod);
        HttpMethod[] supportedMethods = includedPathPatterns.get(requestURI);
        return !Arrays.asList(supportedMethods).contains(method);
    }

    protected abstract boolean handle(HttpServletRequest request);
}
