package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.argumentresolver.LoginMemberArgumentResolver;
import roomescape.argumentresolver.ReservationFilterArgumentResolver;
import roomescape.interceptor.AdminAuthorizationInterceptor;
import roomescape.interceptor.UserAuthorizationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminAuthorizationInterceptor adminAuthorizationInterceptor;
    private final UserAuthorizationInterceptor userAuthorizationInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final ReservationFilterArgumentResolver reservationFilterArgumentResolver;

    public WebConfig(AdminAuthorizationInterceptor adminAuthorizationInterceptor,
                     UserAuthorizationInterceptor userAuthorizationInterceptor,
                     LoginMemberArgumentResolver loginMemberArgumentResolver,
                     ReservationFilterArgumentResolver reservationFilterArgumentResolver) {
        this.adminAuthorizationInterceptor = adminAuthorizationInterceptor;
        this.userAuthorizationInterceptor = userAuthorizationInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.reservationFilterArgumentResolver = reservationFilterArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthorizationInterceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(userAuthorizationInterceptor).addPathPatterns("/reservation");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        resolvers.add(reservationFilterArgumentResolver);
    }
}
