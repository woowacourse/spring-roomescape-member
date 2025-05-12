package roomescape.config;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.infrastructure.intercepter.ResponseHeaderInterceptor;

@Slf4j
@TestConfiguration
public class BypassAuthInterceptorConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(BypassAuthInterceptorConfig.class);

    public BypassAuthInterceptorConfig() {
        logger.info("BypassAuthInterceptorConfig가 초기화되었습니다!");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("커스텀 인터셉터 등록 중...");
        registry.addInterceptor(new ResponseHeaderInterceptor())
                .addPathPatterns("/auth/**");
    }
}
