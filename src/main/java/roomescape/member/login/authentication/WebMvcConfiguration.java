package roomescape.member.login.authentication;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginAuthenticationResolver loginAuthenticationResolver;

    public WebMvcConfiguration(LoginAuthenticationResolver loginAuthenticationResolver) {
        this.loginAuthenticationResolver = loginAuthenticationResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAuthenticationResolver);
    }
}
