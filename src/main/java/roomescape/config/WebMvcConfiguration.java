package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.MemberHandlerMethodArgumentResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberHandlerMethodArgumentResolver memberHandlerMethodArgumentResolver;

    public WebMvcConfiguration(MemberHandlerMethodArgumentResolver memberHandlerMethodArgumentResolver) {
        this.memberHandlerMethodArgumentResolver = memberHandlerMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberHandlerMethodArgumentResolver);
    }
}
