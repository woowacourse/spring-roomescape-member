package roomescape.controller.reservation;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.dto.reservation.ReservationFilterParam;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class ReservationFilterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(ReservationFilterParam.class)
                && parameter.hasParameterAnnotation(ReservationFilter.class);
    }

    @Override
    public ReservationFilterParam resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final Long themeId = Long.parseLong(Objects.requireNonNull(webRequest.getParameter("themeId")));
        final Long memberId = Long.parseLong(Objects.requireNonNull(webRequest.getParameter("memberId")));
        final LocalDate dateFrom = LocalDate.parse(Objects.requireNonNull(webRequest.getParameter("dateFrom")));
        final LocalDate dateTo = LocalDate.parse(Objects.requireNonNull(webRequest.getParameter("dateTo")));

        return new ReservationFilterParam(themeId, memberId, dateFrom, dateTo);
    }
}
