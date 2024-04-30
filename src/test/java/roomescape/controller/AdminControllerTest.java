package roomescape.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminControllerTest {
    @Test
    @DisplayName("관리자 메인 페이지 경로를 정해진 경로로 매핑한다.")
    void mainPage() {
        AdminController adminController = new AdminController();
        String mainPage = adminController.mainPage();
        Assertions.assertThat(mainPage)
                .isEqualTo("admin/index");
    }

    @Test
    @DisplayName("관리자 예약 정보 페이지 경로를 정해진 경로로 매핑한다.")
    void reservationPage() {
        AdminController adminController = new AdminController();
        String reservationPage = adminController.reservationPage();
        Assertions.assertThat(reservationPage)
                .isEqualTo("admin/reservation");
    }

    @Test
    @DisplayName("시간 관리 페이지 경로를 정해진 경로로 매핑한다.")
    void reservationTimePage() {
        AdminController adminController = new AdminController();
        String reservationTimePage = adminController.reservationTimePage();
        Assertions.assertThat(reservationTimePage)
                .isEqualTo("admin/time");
    }
}
