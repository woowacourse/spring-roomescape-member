package roomescape.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.support.auth.MemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(MemberArgumentResolver memberArgumentResolver) {
        this.loginMemberArgumentResolver = memberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
