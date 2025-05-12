package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.support.auth.AdminCheckInterceptor;
import roomescape.support.auth.MemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberArgumentResolver loginMemberArgumentResolver;
    private final AdminCheckInterceptor adminCheckInterceptor;

    public WebConfig(MemberArgumentResolver memberArgumentResolver, AdminCheckInterceptor adminCheckInterceptor) {
        this.loginMemberArgumentResolver = memberArgumentResolver;
        this.adminCheckInterceptor = adminCheckInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns("/admin/**");
    }
}
