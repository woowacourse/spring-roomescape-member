package roomescape.service;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.DuplicateEntityException;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.command.ReservationTimeCommand;
import roomescape.service.result.ReservationTimeResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    @Transactional
    public ReservationTimeResult register(ReservationTimeCommand command) {
        requireNonNull(command, "예약 시간 정보가 필요합니다.");

        validateAlreadyTime(command.startAt());
        ReservationTime reservationTime = new ReservationTime(command.startAt());
        ReservationTime saved = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResult.from(saved);
    }

    @Transactional
    public void remove(long id) {
        reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTimeResult> getAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    private void validateAlreadyTime(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new DuplicateEntityException("이미 등록된 예약 시간 입니다. %s", startAt);
        }
    }
}
