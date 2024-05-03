package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.reservation.dto.ReservationRequest;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.exception.DuplicateReservation;
import roomescape.service.exception.PreviousTimeException;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.TimeNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<ReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse addReservation(final ReservationRequest reservationRequest) {
        final ReservationTime time = findTime(reservationRequest);
        final Theme theme = findTheme(reservationRequest);

        final Reservation parsedReservation = reservationRequest.toDomain(time, theme);
        validateDuplicate(theme, time, parsedReservation);
        final LocalDateTime reservationDateTime = parsedReservation.getDate().atTime(time.getStartAt());
        validateBeforeDay(reservationDateTime); // TODO 같은 날짜지만 시간이 이후인 경우 테스트?

        final Reservation savedReservation = reservationRepository.save(parsedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateBeforeDay(final LocalDateTime reservationDateTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new PreviousTimeException("지난 시간으로 예약할 수 없습니다.");
        }
    }

    private void validateDuplicate(final Theme theme, final ReservationTime time, final Reservation parsedReservation) {
        final boolean isExistsReservation =
                reservationRepository.existsByThemesAndDateAndTimeId(theme.getId(), time.getId(),
                        parsedReservation.getDate());
        if (isExistsReservation) {
            throw new DuplicateReservation("중복된 시간으로 예약이 불가합니다.");
        }
    }

    private Theme findTheme(final ReservationRequest reservationRequest) {
        return themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new ThemeNotFoundException("존재하지 않는 테마입니다."));
    }

    private ReservationTime findTime(final ReservationRequest reservationRequest) {
        return reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new TimeNotFoundException("존재하지 않은 시간입니다."));
    }

    public int deleteReservation(final Long id) {
        return reservationRepository.deleteById(id);
    }
}
