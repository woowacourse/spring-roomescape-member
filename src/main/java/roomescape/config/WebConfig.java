package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    MemberArgumentResolver memberArgumentResolver;
    ReservationArgumentResolver reservationArgumentResolver;

    public WebConfig(MemberArgumentResolver memberArgumentResolver,
            ReservationArgumentResolver reservationArgumentResolver) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.reservationArgumentResolver = reservationArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
        resolvers.add(reservationArgumentResolver);
    }

}
