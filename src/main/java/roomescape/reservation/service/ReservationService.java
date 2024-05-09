package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.dto.SaveReservationRequest;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationDate;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            final ReservationRepository reservationRepository,
            final ReservationTimeRepository reservationTimeRepository,
            final ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation saveReservation(final SaveReservationRequest request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
        final Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NoSuchElementException("해당 id의 테마가 존재하지 않습니다."));

        final Reservation reservation = request.toReservation(reservationTime, theme);
        validateReservationDateAndTime(reservation.getDate(), reservationTime);
        validateReservationDuplication(request);

        return reservationRepository.save(request.toReservation(reservationTime, theme));
    }

    private static void validateReservationDateAndTime(final ReservationDate date, final ReservationTime time) {
        final LocalDateTime reservationLocalDateTime = LocalDateTime.of(date.value(), time.getStartAt());
        if (reservationLocalDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재 날짜보다 이전 날짜를 예약할 수 없습니다.");
        }
    }

    private void validateReservationDuplication(final SaveReservationRequest request) {
        if (reservationRepository.existByDateAndTimeIdAndThemeId(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 해당 날짜/시간의 테마 예약이 있습니다.");
        }
    }

    public void deleteReservation(final Long reservationId) {
        final int deletedDataCount = reservationRepository.deleteById(reservationId);

        if (deletedDataCount <= 0) {
            throw new NoSuchElementException("해당 id의 예약이 존재하지 않습니다.");
        }
    }
}
