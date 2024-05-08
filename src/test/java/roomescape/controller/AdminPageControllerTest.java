package roomescape.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminPageControllerTest {
    @Test
    @DisplayName("관리자 메인 페이지 경로를 정해진 경로로 매핑한다.")
    void mainPage() {
        AdminPageController adminPageController = new AdminPageController();
        String mainPage = adminPageController.mainPage();
        Assertions.assertThat(mainPage)
                .isEqualTo("admin/index");
    }

    @Test
    @DisplayName("관리자 예약 정보 페이지 경로를 정해진 경로로 매핑한다.")
    void reservationPage() {
        AdminPageController adminPageController = new AdminPageController();
        String reservationPage = adminPageController.reservationPage();
        Assertions.assertThat(reservationPage)
                .isEqualTo("admin/reservation-new");
    }

    @Test
    @DisplayName("시간 관리 페이지 경로를 정해진 경로로 매핑한다.")
    void reservationTimePage() {
        AdminPageController adminPageController = new AdminPageController();
        String reservationTimePage = adminPageController.reservationTimePage();
        Assertions.assertThat(reservationTimePage)
                .isEqualTo("admin/time");
    }
}
