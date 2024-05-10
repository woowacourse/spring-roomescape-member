package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.controller.MemberArgumentResolver;
import roomescape.member.service.AuthService;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public WebMvcConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        MemberArgumentResolver resolver = new MemberArgumentResolver(authService);
        resolvers.add(resolver);
    }
}
