package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PageControllerTest {

    private final PageController pageController = new PageController();

    @DisplayName("웹컴페이지 요청시 메인페이지로 리다이렉션한다")
    @Test
    void requestWelcomePage() {
        ResponseEntity<Void> response = pageController.getWelcomePage();
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PERMANENT_REDIRECT),
                () -> assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/admin")
        );
    }

    @DisplayName("메인페이지 요청시 메인페이지 뷰 이름을 리턴한다")
    @Test
    void requestMainPage() {
        String mainPageViewName = pageController.getMainPage();
        assertThat(mainPageViewName).isEqualTo("admin/index");
    }

    @DisplayName("예약페이지 요청시 예약페이지 뷰 이름을 리턴한다")
    @Test
    void requestReservationPage() {
        String reservationPageViewName = pageController.getReservationPage();
        assertThat(reservationPageViewName).isEqualTo("admin/reservation");
    }

    @DisplayName("시간페이지 요청시 시간페이지 뷰 이름을 리턴한다.")
    @Test
    void requestTimePage() {
        String timePageViewName = pageController.getTimePage();
        assertThat(timePageViewName).isEqualTo("admin/time");
    }
}
