package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.ReservationTimeCreation;
import roomescape.service.dto.response.AvailableReservationTimeResult;
import roomescape.service.dto.response.ReservationTimeResult;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResult addReservationTime(final ReservationTimeCreation creation) {
        if (reservationTimeRepository.existsByStartAt(creation.startAt())) {
            throw new ExistedDuplicateValueException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
        final ReservationTime reservationTime = new ReservationTime(creation.startAt());
        final long id = reservationTimeRepository.insert(reservationTime);

        final ReservationTime savedReservationTime = findById(id);

        return ReservationTimeResult.from(savedReservationTime);
    }

    private ReservationTime findById(final long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 예약 시간입니다"));
    }

    public List<ReservationTimeResult> findAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResult::from)
                .toList();
    }

    public List<AvailableReservationTimeResult> findAllAvailableTime(final LocalDate date, final long themeId) {
        List<ReservationTime> totalReservationTime = reservationTimeRepository.findAll();
        Set<ReservationTime> bookedTime = new HashSet<>(reservationTimeRepository.findAllBookedTime(date, themeId));
        List<AvailableReservationTimeResult> responses = new ArrayList<>();

        for (ReservationTime reservationTime : totalReservationTime) {
            boolean isBooked = bookedTime.contains(reservationTime);
            responses.add(AvailableReservationTimeResult.of(reservationTime, isBooked));
        }
        return responses;
    }

    public void deleteById(final long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new BusinessRuleViolationException("사용 중인 예약 시간입니다");
        }

        boolean deleted = reservationTimeRepository.deleteById(id);

        if (!deleted) {
            throw new NotFoundValueException("존재하지 않는 예약 시간입니다");
        }
    }
}
