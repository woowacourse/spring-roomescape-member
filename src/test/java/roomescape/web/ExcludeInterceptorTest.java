package roomescape.web;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.SpyBean;
import roomescape.web.api.resolver.AdminAuthValidateInterceptor;
import roomescape.web.api.resolver.MemberAuthValidateInterceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

abstract class ExcludeInterceptorTest extends ControllerTest {

    @SpyBean
    private MemberAuthValidateInterceptor memberAuthValidateInterceptor;
    @SpyBean
    private AdminAuthValidateInterceptor adminAuthValidateInterceptor;

    @BeforeEach
    void setUp() {
        doReturn(true).when(memberAuthValidateInterceptor).preHandle(any(), any(), any());
        doReturn(true).when(adminAuthValidateInterceptor).preHandle(any(), any(), any());
    }
}
