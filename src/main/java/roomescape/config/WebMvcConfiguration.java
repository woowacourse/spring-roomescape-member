package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.interceptor.CheckAdminMemberInterceptor;
import roomescape.resolver.AuthMemberArgumentResolver;
import roomescape.util.CookieProvider;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final CookieProvider cookieProvider;

    public WebMvcConfiguration(final CookieProvider cookieProvider) {
        this.cookieProvider = cookieProvider;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAdminMemberInterceptor(cookieProvider))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthMemberArgumentResolver(cookieProvider));
    }
}
