package roomescape;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.AdminInterceptor;
import roomescape.controller.LoginMemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(
            final LoginMemberArgumentResolver loginMemberArgumentResolver,
            final AdminInterceptor adminInterceptor
    ) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin")
                .addPathPatterns("/admin/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
