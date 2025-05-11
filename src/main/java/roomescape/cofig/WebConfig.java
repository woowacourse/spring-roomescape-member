package roomescape.cofig;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.LoginMemberArgumentReslover;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentReslover loginMemberArgumentReslover;

    public WebConfig(LoginMemberArgumentReslover loginMemberArgumentReslover) {
        this.loginMemberArgumentReslover = loginMemberArgumentReslover;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentReslover);
    }
}
