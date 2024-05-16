package roomescape.web.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.web.api.resolver.AdminAuthValidateInterceptor;
import roomescape.web.api.resolver.MemberArgumentResolver;
import roomescape.web.api.resolver.MemberAuthValidateInterceptor;
import roomescape.web.api.token.TokenParser;

import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberArgumentResolver memberArgumentResolver;
    private final TokenParser tokenParser;

    public WebMvcConfiguration(
            MemberArgumentResolver memberArgumentResolver,
            TokenParser tokenParser
    ) {
        this.memberArgumentResolver = memberArgumentResolver;
        this.tokenParser = tokenParser;
    }

    @Bean
    public AdminAuthValidateInterceptor adminAuthValidateInterceptor(TokenParser tokenParser) {
        return (AdminAuthValidateInterceptor) new AdminAuthValidateInterceptor(tokenParser)
                .addPathPatterns("/times", POST)
                .addPathPatterns("/times/", DELETE)
                .addPathPatterns("/reservations", GET)
                .addPathPatterns("/reservations/", DELETE)
                .addPathPatterns("/admin/reservations", GET, POST)
                .addPathPatterns("/themes", POST)
                .addPathPatterns("/themes/", DELETE)
                .addPathPatterns("/members", GET);
    }

    @Bean
    public MemberAuthValidateInterceptor memberAuthValidateInterceptor(TokenParser tokenParser) {
        return (MemberAuthValidateInterceptor) new MemberAuthValidateInterceptor(tokenParser)
                .addPathPatterns("/reservations", POST);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthValidateInterceptor(tokenParser));
        registry.addInterceptor(memberAuthValidateInterceptor(tokenParser));
    }
}
