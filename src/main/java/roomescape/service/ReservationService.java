package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.EntityNotFoundException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.global.auth.Accessor;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.command.ReservationCommand;
import roomescape.service.result.ReservationResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ReservationResult reserve(Accessor accessor, ReservationCommand command) {
        accessor.validateAdmin();

        ReservationTime time = findTimeWithThrow(command.timeId());
        validateAlreadyReservation(command.date(), command.timeId(), time);

        Reservation reservation = new Reservation(command.name(), command.date(), time);
        Reservation saved = reservationRepository.save(reservation);

        return ReservationResult.from(saved);
    }

    @Transactional
    public void cancelReservation(Accessor accessor, long id) {
        accessor.validateAdmin();
        reservationRepository.delete(id);
    }

    public List<ReservationResult> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResult::from)
                .toList();
    }

    private void validateAlreadyReservation(LocalDate date, long timeId, ReservationTime time) {
        if (reservationRepository.existByDateAndTimeId(date, timeId)) {
            throw new DuplicateEntityException("이미 예약 된 날짜입니다. (%s-%s)", date, time.getStartAt());
        }
    }

    private ReservationTime findTimeWithThrow(long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 시간 정보입니다."));
    }
}
