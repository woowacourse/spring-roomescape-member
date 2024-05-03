package roomescape.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private static Theme convertToTheme(ThemeResponse themeResponse) {
        return new Theme(themeResponse.id(),
                new Theme(themeResponse.name(), themeResponse.description(), themeResponse.thumbnail()));
    }

    @AfterEach
    void init() {
        for (Reservation reservation : reservationRepository.findAll()) {
            reservationRepository.deleteById(reservation.getId());
        }
        for (ThemeResponse themeResponse : themeService.findAll()) {
            themeService.deleteById(themeResponse.id());
        }
    }

    @DisplayName("테마를 생성한다.")
    @Test
    void create() {
        //given
        ThemeResponse themeResponse = createTheme("레벨2 탈출");

        //then
        assertThat(themeResponse.id()).isNotZero();
    }

    @DisplayName("테마를 생성한다.")
    @Test
    void cannotCreateByDuplicatedName() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeService.create(themeRequest);

        //when&then
        assertThatThrownBy(() -> themeService.create(themeRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이미 존재하는 테마 이름입니다.");
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void findAll() {
        //given
        createTheme("레벨2 탈출");

        //when
        List<ThemeResponse> responses = themeService.findAll();

        //then
        assertThat(responses).hasSize(1);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteById() {
        //given
        ThemeResponse target = createTheme("레벨2 탈출");

        //when
        themeService.deleteById(target.id());

        //then
        assertThat(themeService.findAll()).hasSize(0);
    }

    @DisplayName("예약이 존재하는 테마를 삭제하면 예외가 발생한다.")
    @Test
    void cannotDeleteByReservation() {
        //given
        ThemeResponse themeResponse = createTheme("레벨2 탈출");
        Theme theme = convertToTheme(themeResponse);
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime("21:25"));
        reservationRepository.save(new Reservation("lini", new ReservationDate("2024-10-04"), reservationTime, theme));

        //when&then
        assertThatThrownBy(() -> themeService.deleteById(theme.getId()))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("해당 테마로 예약이 존재해서 삭제할 수 없습니다.");
    }

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void findPopularThemes() {
        //given
        ThemeResponse theme1 = createTheme("레벨1 탈출");
        ThemeResponse theme2 = createTheme("레벨2 탈출");
        ThemeResponse theme3 = createTheme("레벨3 탈출");

        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime("21:25"));
        reservationRepository.save(
                new Reservation("lini", new ReservationDate(LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)),
                        reservationTime, convertToTheme(theme1)));
        reservationRepository.save(
                new Reservation("lini", new ReservationDate(LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_DATE)),
                        reservationTime, convertToTheme(theme2)));
        reservationRepository.save(
                new Reservation("lini", new ReservationDate(LocalDate.now().minusDays(8).format(DateTimeFormatter.ISO_DATE)),
                        reservationTime, convertToTheme(theme3)));

        //when
        List<ThemeResponse> result = themeService.findPopularThemes();

        //then
        assertThat(result).hasSize(2);
    }

    private ThemeResponse createTheme(String name) {
        ThemeRequest themeRequest = new ThemeRequest(name, "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        return themeService.create(themeRequest);
    }
}
