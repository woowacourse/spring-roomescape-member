package roomescape.theme.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.theme.service.UserThemeService;

@WebMvcTest(UserThemeController.class)
class UserThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserThemeService userThemeService;

    @Test
    void 날짜_파라미터_없으면_오늘_기준_기본값이_적용된다() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate sixDaysAgo = today.minusDays(6);

        when(userThemeService.getThemes(
                eq(SortColumn.RESERVATION_COUNT), eq(SortOrder.DESC),
                eq(sixDaysAgo), eq(today), eq(10L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes/rank"))
                .andExpect(status().isOk());

        verify(userThemeService).getThemes(
                eq(SortColumn.RESERVATION_COUNT), eq(SortOrder.DESC),
                eq(sixDaysAgo), eq(today), eq(10L));
    }
}