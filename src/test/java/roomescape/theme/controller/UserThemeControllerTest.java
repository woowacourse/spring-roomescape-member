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
import roomescape.theme.domain.SortType;
import roomescape.theme.domain.SortOrder;
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
                eq(SortType.RESERVATION_COUNT), eq(SortOrder.DESC),
                eq(sixDaysAgo), eq(today), eq(10L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes/rank"))
                .andExpect(status().isOk());

        verify(userThemeService).getThemes(
                eq(SortType.RESERVATION_COUNT), eq(SortOrder.DESC),
                eq(sixDaysAgo), eq(today), eq(10L));
    }

    @Test
    void endDate만_주어지면_startDate는_endDate_기준_6일_전으로_계산된다() throws Exception {
        LocalDate endDate = LocalDate.of(2099, 12, 31);
        LocalDate expectedStartDate = endDate.minusDays(6);

        when(userThemeService.getThemes(
                eq(SortType.RESERVATION_COUNT), eq(SortOrder.DESC),
                eq(expectedStartDate), eq(endDate), eq(10L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes/rank").param("endDate", "2099-12-31"))
                .andExpect(status().isOk());

        verify(userThemeService).getThemes(
                eq(SortType.RESERVATION_COUNT), eq(SortOrder.DESC),
                eq(expectedStartDate), eq(endDate), eq(10L));
    }
}