package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.RoomThemeRepository;
import roomescape.service.dto.ReservationCreation;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final RoomThemeRepository roomThemeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository,
                              final RoomThemeRepository roomThemeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.roomThemeRepository = roomThemeRepository;
    }

    public ReservationResponse addReservation(final ReservationCreation creation) {
        final ReservationTime reservationTime = findReservationTimeByTimeId(creation.timeId());
        final RoomTheme theme = findThemeByThemeId(creation.themeId());
        final Reservation reservation = new Reservation(creation.name(), creation.date(), reservationTime, theme);

        validatePastDateAndTime(reservation.getDate(), reservation.getTime());
        validateDuplicateReservation(reservation);

        final long savedId = reservationRepository.insert(reservation);
        final Reservation savedReservation = findById(savedId);

        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime findReservationTimeByTimeId(final long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 예약 가능 시간입니다: timeId=%d"
                        .formatted(timeId)));
    }

    private RoomTheme findThemeByThemeId(final long themeId) {
        return roomThemeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 테마 입니다"));
    }

    private void validatePastDateAndTime(final LocalDate date, final ReservationTime time) {
        final LocalDate currentDate = LocalDate.now();

        final boolean isPastDate = date.isBefore(currentDate);
        final boolean isPastTime = date.isEqual(currentDate) && time.getStartAt().isBefore(LocalTime.now());

        if (isPastDate || isPastTime) {
            throw new BusinessRuleViolationException("과거 시점은 예약할 수 없습니다");
        }
    }

    private void validateDuplicateReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new ExistedDuplicateValueException("이미 예약이 존재하는 시간입니다: date=%s, time=%s"
                    .formatted(reservation.getDate(), reservation.getTime().getStartAt()));
        }
    }

    private boolean existsSameReservation(final Reservation reservation) {
        return reservationRepository.existSameReservation(reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId());
    }

    private Reservation findById(final long savedId) {
        return reservationRepository.findById(savedId)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 예약입니다"));
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void removeReservationById(final long id) {
        boolean deleted = reservationRepository.deleteById(id);

        if (!deleted) {
            throw new NotFoundValueException("존재하지 않는 예약입니다");
        }
    }
}
