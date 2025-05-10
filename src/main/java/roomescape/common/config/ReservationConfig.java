package roomescape.common.config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.common.argumentResolver.LoginArgumentResolver;
import roomescape.common.util.TokenCookieManager;
import roomescape.member.service.LoginService;

@Configuration
public class ReservationConfig implements WebMvcConfigurer {

    private final LoginService loginService;
    private final TokenCookieManager tokenCookieManager;

    public ReservationConfig(LoginService loginService, TokenCookieManager tokenCookieManager) {
        this.loginService = loginService;
        this.tokenCookieManager = tokenCookieManager;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(loginService, tokenCookieManager));
    }
}
