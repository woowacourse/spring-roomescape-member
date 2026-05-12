package roomescape.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.InvalidReservationDateException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.PopularThemesResult;
import roomescape.reservation.service.dto.ReservationCommand;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.exception.InvalidTimeStartAtException;
import roomescape.time.repository.ReservationTimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public Reservation makeReservation(ReservationCommand command) {
        ReservationTime time = reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약 시간이 존재하지 않습니다."));

        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 테마가 존재하지 않습니다."));

        LocalDate nowDate = LocalDate.now(clock);

        if (nowDate.isAfter(command.date())) {
            throw new InvalidReservationDateException();
        }

        if (nowDate.equals(command.date()) && LocalTime.now(clock).isAfter(time.getStartAt())) {
            throw new InvalidTimeStartAtException();
        }

        return reservationRepository.save(
                Reservation.of(command.name(), command.date(), time, theme)
        );
    }

    @Transactional
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findReservationsByName(String name) {
        return reservationRepository.findAllByName(name);
    }

    public List<Reservation> findReservations() {
        return reservationRepository.findAll();
    }

    public PopularThemesResult findPopularThemes(int period, int limit) {
        LocalDate to = LocalDate.now(clock).minusDays(1);
        LocalDate from = to.minusDays(period - 1);

        return new PopularThemesResult(
                reservationRepository.findPopularThemes(from, to, limit)
        );
    }
}
