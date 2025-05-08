package roomescape.common.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.common.argument.MemberArgumentResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberArgumentResolver memberArgumentResolver;

    @Autowired
    public WebConfig(MemberArgumentResolver memberArgumentResolver) {
        this.memberArgumentResolver = memberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
