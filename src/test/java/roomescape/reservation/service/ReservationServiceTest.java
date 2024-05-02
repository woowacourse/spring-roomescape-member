package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.reservation.dao.FakeMemberDao;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.FakeReservationTimeDao;
import roomescape.reservation.dao.FakeThemeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@DisplayName("예약 로직 테스트")
class ReservationServiceTest {
    ReservationRepository reservationRepository;
    ReservationTimeRepository reservationTimeRepository;
    ThemeRepository themeRepository;
    MemberRepository memberRepository;
    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationDao();
        reservationTimeRepository = new FakeReservationTimeDao(reservationRepository);
        themeRepository = new FakeThemeDao(reservationRepository);
        memberRepository = new FakeMemberDao();
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                themeRepository,
                memberRepository
        );
    }

    @DisplayName("예약 생성에 성공한다.")
    @Test
    void create() {
        //given
        String name = "choco";
        String date = "2100-04-18";
        long timeId = 1L;
        long themeId = 1L;
        reservationTimeRepository.save(new ReservationTime(timeId, LocalTime.MIDNIGHT));
        themeRepository.save(new Theme(themeId, "name", "description", "thumbnail"));
        ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, themeId);

        //when
        ReservationResponse reservationResponse = reservationService.create(reservationRequest);

        //then
        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo(name),
                () -> assertThat(reservationResponse.date()).isEqualTo(date),
                () -> assertThat(reservationResponse.time().id()).isEqualTo(timeId)
        );
    }

    @DisplayName("예약 조회에 성공한다.")
    @Test
    void find() {
        //given
        long id = 1;
        String name = "choco";
        LocalDate date = LocalDate.now().plusYears(1);
        long timeId = 1L;
        long themeId = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        ReservationTime reservationTime = new ReservationTime(timeId, localTime);

        Theme theme = new Theme(themeId, "name", "description", "thumbnail");
        reservationRepository.save(new Reservation(id, name, date, reservationTime, theme));

        //when
        List<ReservationResponse> reservations = reservationService.findAllReservations();

        //then
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).name()).isEqualTo(name),
                () -> assertThat(reservations.get(0).date()).isEqualTo(date),
                () -> assertThat(reservations.get(0).time().id()).isEqualTo(timeId),
                () -> assertThat(reservations.get(0).time().startAt()).isEqualTo(localTime)
        );
    }

    @DisplayName("예약 삭제에 성공한다.")
    @Test
    void delete() {
        //given
        long id = 1;
        String name = "choco";
        LocalDate date = LocalDate.now().plusYears(1);
        long timeId = 1L;
        long themeId = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        ReservationTime reservationTime = new ReservationTime(timeId, localTime);

        Theme theme = new Theme(themeId, "name", "description", "thumbnail");
        reservationRepository.save(new Reservation(id, name, date, reservationTime, theme));

        //when
        reservationService.delete(id);

        //then
        assertThat(reservationRepository.findAll()).hasSize(0);
    }

    @DisplayName("일자와 시간 중복 시 예외가 발생한다.")
    @Test
    void duplicatedReservation() {
        //given
        String name = "choco";
        String date = "2099-04-18";
        long timeId = 1L;
        long themeId = 1L;
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(timeId, LocalTime.MIDNIGHT));
        Theme theme = new Theme(themeId, "name", "description", "thumbnail");
        themeRepository.save(theme);
        reservationRepository.save(new Reservation(1L, "choco", LocalDate.parse(date), time, theme));

        ReservationRequest reservationRequest = new ReservationRequest(name, date, timeId, themeId);

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
