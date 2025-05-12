package roomescape.cofig;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.infra.JwtProvider;
import roomescape.member.dao.MemberDao;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentReslover loginMemberArgumentReslover;
    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public WebConfig(LoginMemberArgumentReslover loginMemberArgumentReslover, MemberDao memberDao, JwtProvider jwtProvider) {
        this.loginMemberArgumentReslover = loginMemberArgumentReslover;
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentReslover);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminCheckInterceptor(memberDao, jwtProvider))
                .addPathPatterns("/admin/**");
    }
}
