package roomescape.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.EntityNotFoundException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ReservationCommand;
import roomescape.service.result.ReservationResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional
    public ReservationResult reserve(ReservationCommand command) {
        Theme theme = findThemeWithThrow(command);
        ReservationTime time = findTimeWithThrow(command);
        validateAlreadyReservation(command, time);

        Reservation reservation = Reservation.reserve(command.name(), command.date(), theme, time);
        Reservation saved = reservationRepository.save(reservation);

        return ReservationResult.from(saved);
    }

    @Transactional
    public void cancelReservation(long id) {
        reservationRepository.delete(id);
    }

    public List<ReservationResult> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResult::from)
                .toList();
    }

    private void validateAlreadyReservation(ReservationCommand command, ReservationTime time) {
        if (reservationRepository.existByDateAndTimeId(command.date(), command.timeId())) {
            throw new DuplicateEntityException("이미 예약 된 날짜입니다. (%s-%s)", command.date(), time.getStartAt());
        }
    }

    private Theme findThemeWithThrow(ReservationCommand command) {
        return themeRepository.findById(command.themeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 테마 정보입니다."));
    }

    private ReservationTime findTimeWithThrow(ReservationCommand command) {
        return reservationTimeRepository.findById(command.timeId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 시간 정보입니다."));
    }
}
