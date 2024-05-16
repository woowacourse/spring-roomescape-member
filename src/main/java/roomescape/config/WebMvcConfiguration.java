package roomescape.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.member.service.MemberService;
import roomescape.ui.LoginMemberArgumentResolver;
import roomescape.ui.AdminPermissionInterceptor;
import roomescape.util.JwtTokenHelper;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    private final MemberService memberService;

    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Bean
    public JwtTokenHelper jwtTokenHelper() {
        return new JwtTokenHelper(secretKey, validityInMilliseconds);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtTokenHelper()));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminPermissionInterceptor())
                .addPathPatterns("/admin/**");
    }

    @Bean
    public AdminPermissionInterceptor adminPermissionInterceptor() {
        return new AdminPermissionInterceptor(memberService, jwtTokenHelper());
    }
}
