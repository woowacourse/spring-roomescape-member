package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    MemberArgumentResolver memberArgumentResolver;
    ReservationArgumentResolver reservationArgumentResolver;
    CheckRoleInterceptor checkRoleInterceptor;

    public WebConfig(MemberArgumentResolver memberArgumentResolver,
            ReservationArgumentResolver reservationArgumentResolver, CheckRoleInterceptor checkRoleInterceptor) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.reservationArgumentResolver = reservationArgumentResolver;
        this.checkRoleInterceptor = checkRoleInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
        resolvers.add(reservationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkRoleInterceptor)
                .addPathPatterns("/admin/**");
    }

}
