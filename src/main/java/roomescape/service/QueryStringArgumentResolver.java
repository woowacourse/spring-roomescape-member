package roomescape.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.controller.RequestParams;

@Component
public class QueryStringArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    public QueryStringArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(RequestParams.class) != null;
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @Nullable NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        if (parameter == null || webRequest == null) {
            return null;
        }
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        try {
            String json = convertQueryToJson(request.getQueryString());
            return objectMapper.readValue(json, parameter.getParameterType());
        } catch (Exception e) {
            return null;
        }
    }

    private String convertQueryToJson(String query) {
        StringBuilder res = new StringBuilder("{\"");

        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == '=') {
                res.append("\":\"");
                continue;
            }
            if (query.charAt(i) == '&') {
                res.append("\",\"");
                continue;
            }
            res.append(query.charAt(i));
        }
        res.append("\"}");

        return res.toString();
    }
}
