package roomescape.core.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.domain.Theme;
import roomescape.core.dto.BookingTimeResponseDto;
import roomescape.core.dto.ReservationRequestDto;
import roomescape.core.dto.ReservationResponseDto;
import roomescape.core.repository.ReservationRepository;
import roomescape.core.repository.ReservationTimeRepository;
import roomescape.core.repository.ThemeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationResponseDto create(final ReservationRequestDto request) {
        final ReservationTime reservationTime = reservationTimeRepository.findById(request.getTimeId());
        final Theme theme = themeRepository.findById(request.getThemeId());
        final Reservation reservation = new Reservation(request.getName(), request.getDate(), reservationTime, theme);

        validateDateTimeIsNotPast(reservation, reservationTime);
        validateDuplicatedReservation(reservation, reservationTime);
        final Long id = reservationRepository.save(reservation);

        return new ReservationResponseDto(id, reservation);
    }

    private void validateDateTimeIsNotPast(final Reservation reservation, final ReservationTime reservationTime) {
        if (reservation.isDatePast()) {
            throw new IllegalArgumentException("지난 날짜에는 예약할 수 없습니다.");
        }
        if (reservation.isDateToday() && reservationTime.isPast()) {
            throw new IllegalArgumentException("지난 시간에는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicatedReservation(final Reservation reservation, final ReservationTime reservationTime) {
        final boolean exist = reservationRepository.existByDateAndTimeIdAndThemeId(
                reservation.getDateString(),
                reservationTime.getId(),
                reservation.getTheme().getId()
        );
        if (exist) {
            throw new IllegalArgumentException("예약 내역이 존재합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingTimeResponseDto> findBookable(final String date, final long themeId) {
        return reservationRepository.findAllByDateAndThemeId(date, themeId);
    }

    @Transactional
    public void delete(final long id) {
        reservationRepository.deleteById(id);
    }
}
