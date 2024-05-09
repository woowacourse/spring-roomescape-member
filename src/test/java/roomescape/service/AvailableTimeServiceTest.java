package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Sha256Encryptor;
import roomescape.domain.Theme;
import roomescape.dto.AvailableTimeResponse;
import roomescape.repository.CollectionReservationRepository;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.CollectionThemeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

class AvailableTimeServiceTest {

    private AvailableTimeService availableTimeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void init() {
        reservationRepository = new CollectionReservationRepository();
        reservationTimeRepository = new CollectionReservationTimeRepository(reservationRepository);
        themeRepository = new CollectionThemeRepository();
        availableTimeService = new AvailableTimeService(reservationTimeRepository, themeRepository);
    }

    @DisplayName("날짜와 테마, 시간에 대한 예약 내역을 확인할 수 있다.")
    @Test
    void findAvailableTimeTest() {
        //given
        Theme DEFUALT_THEME = new Theme(1L, "name", "description", "http://thumbnail");
        themeRepository.save(DEFUALT_THEME);
        ReservationTime reservationTime1 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(12, 0)));
        ReservationTime reservationTime3 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(13, 0)));
        ReservationTime reservationTime4 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(14, 0)));

        LocalDate selectedDate = LocalDate.of(2024, 1, 1);
        Member member = new Member(1L, "name", "email@email.com", new Sha256Encryptor().encrypt("1234"));
        reservationRepository.save(new Reservation(member, selectedDate, reservationTime1, DEFUALT_THEME));
        reservationRepository.save(new Reservation(member, selectedDate, reservationTime3, DEFUALT_THEME));

        //when
        List<AvailableTimeResponse> availableTimeResponses = availableTimeService.findByThemeAndDate(selectedDate,
                DEFUALT_THEME.getId());

        //then
        assertThat(availableTimeResponses).containsExactlyInAnyOrder(
                new AvailableTimeResponse(1L, reservationTime1.getStartAt(), true),
                new AvailableTimeResponse(2L, reservationTime2.getStartAt(), false),
                new AvailableTimeResponse(3L, reservationTime3.getStartAt(), true),
                new AvailableTimeResponse(4L, reservationTime4.getStartAt(), false)
        );
    }
}
