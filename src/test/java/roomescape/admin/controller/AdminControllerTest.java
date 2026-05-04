package roomescape.admin.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.RequestReservationTime;
import roomescape.time.dto.ResponseReservationTime;
import roomescape.time.service.ReservationTimeService;

import java.time.LocalTime;
import java.util.List;

class AdminControllerTest {

    private AdminController adminController;
    private FakeThemeService fakeThemeService;
    private FakeReservationTime fakeReservationTimeService;

    @BeforeEach
    public void setUp() {
        fakeThemeService = new FakeThemeService();
        fakeReservationTimeService = new FakeReservationTime();
        adminController = new AdminController(fakeThemeService, fakeReservationTimeService);
    }

    @Test
    void 시간_생성_요청을_받으면_DTO의_시작_시간을_Service에_전달하고_결과를_반환한다() {
        RequestReservationTime request = new RequestReservationTime(LocalTime.of(10, 0));
        ReservationTime created = new ReservationTime(1L, LocalTime.of(10, 0));
        fakeReservationTimeService.toReturn = created;

        ResponseReservationTime result = adminController.createTime(request);

        Assertions.assertThat(fakeReservationTimeService.capturedStartAt).isEqualTo(LocalTime.of(10, 0));
        Assertions.assertThat(result.id()).isEqualTo(1L);
        Assertions.assertThat(result.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 시간_삭제_요청을_받으면_PathVariable_id를_Service에_전달한다() {
        adminController.removeTime(3L);

        Assertions.assertThat(fakeReservationTimeService.removedId).isEqualTo(3L);
    }

    private static class FakeReservationTime implements ReservationTimeService {
        LocalTime capturedStartAt;
        Long removedId;
        List<ReservationTime> toReturnTimes = List.of();
        ReservationTime toReturn;

        @Override
        public List<ReservationTime> getTimes() {
            return toReturnTimes;
        }

        @Override
        public ReservationTime createTime(LocalTime localTime) {
            this.capturedStartAt = localTime;
            return toReturn;
        }

        @Override
        public void removeTime(Long id) {
            this.removedId = id;
        }
    }

    private static class FakeThemeService implements ThemeService {
        Long removedId;
        String name;
        String description;
        String thumbnail;
        Theme toReturn;

        @Override
        public Theme createTheme(String name, String description, String thumbnail) {
            this.name = name;
            this.description = description;
            this.thumbnail = thumbnail;
            return toReturn;
        }

        @Override
        public void removeTheme(Long id) {
            this.removedId = id;
        }

        @Override
        public List<Theme> getThemes() {
            return List.of();
        }
    }
}