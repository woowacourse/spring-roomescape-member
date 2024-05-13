package roomescape.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.request.FilteredReservationRequest;

public class FilteredReservationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(FilteredReservationRequest.class);
    }

    @Override
    public FilteredReservationRequest resolveArgument(MethodParameter parameter,
                                                      ModelAndViewContainer mavContainer,
                                                      NativeWebRequest webRequest,
                                                      WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return new FilteredReservationRequest(
                Long.valueOf(request.getParameter("theme-id")),
                Long.valueOf(request.getParameter("member-id")),
                LocalDate.parse(request.getParameter("date-from")),
                LocalDate.parse(request.getParameter("date-to")));
    }
}
