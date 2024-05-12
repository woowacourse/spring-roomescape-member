package roomescape.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.member.dto.MemberProfileInfo;
import roomescape.member.security.service.MemberAuthService;
import roomescape.reservation.dto.ReservationRequest;

@Component
public class ReservationArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberAuthService memberAuthService;

    public ReservationArgumentResolver(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType()
                .equals(ReservationRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        MemberProfileInfo payload = memberAuthService.extractPayload(request.getCookies());

        ReservationRequest reservationRequest = convertToRequestBody(request);
        return new ReservationRequest(
                LocalDate.from(reservationRequest.date()),
                payload.name(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );
    }

    private ReservationRequest convertToRequestBody(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
        ) {
            return objectMapper.readValue(reader, ReservationRequest.class);
        }
    }

}
