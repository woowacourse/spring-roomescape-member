package roomescape.resolver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import roomescape.dto.request.ReservationDetailRequest;

public class ReservationDetailArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ReservationDetailRequest.class);
    }

    @Override
    public ReservationDetailRequest resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Long themeId = Long.valueOf(request.getParameter("themeId"));
        Long memberId = Long.valueOf(request.getParameter("memberId"));
        LocalDate dateFrom = LocalDate.parse(request.getParameter("dateFrom"), formatter);
        LocalDate dateTo = LocalDate.parse(request.getParameter("dateTo"), formatter);

        return new ReservationDetailRequest(themeId, memberId, dateFrom, dateTo);
    }
}
