package roomescape.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.response.AvailableReservationTimeResponse;
import roomescape.controller.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreation;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationRepository reservationRepository,
                                  final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse addReservationTime(final ReservationTimeCreation creation) {
        if (reservationTimeRepository.existsByStartAt(creation.startAt())) {
            throw new ExistedDuplicateValueException("이미 존재하는 예약 가능 시간입니다: %s".formatted(creation.startAt()));
        }
        final ReservationTime reservationTime = new ReservationTime(creation.startAt());
        final long id = reservationTimeRepository.insert(reservationTime);

        final ReservationTime savedReservationTime = findById(id);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    private ReservationTime findById(final long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundValueException("존재하지 않는 예약 시간입니다"));
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAllAvailableTime(final LocalDate date, final long themeId) {
        List<ReservationTime> totalReservationTime = reservationTimeRepository.findAll();
        List<ReservationTime> bookedTime = reservationTimeRepository.findAllBookedTime(date, themeId);
        List<AvailableReservationTimeResponse> responses = new ArrayList<>();

        for (ReservationTime reservationTime : totalReservationTime) {
            if (bookedTime.contains(reservationTime)) {
                responses.add(AvailableReservationTimeResponse.from(reservationTime, true));
                continue;
            }
            responses.add(AvailableReservationTimeResponse.from(reservationTime, false));
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
