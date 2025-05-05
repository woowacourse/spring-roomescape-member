package roomescape.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.application.dto.request.CreateReservationTimeServiceRequest;
import roomescape.application.dto.response.ReservationTimeServiceResponse;
import roomescape.domain.entity.ReservationTime;
import roomescape.domain.exception.ReservationException;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.service.ReservationTimeValidationService;
import roomescape.global.exception.BusinessRuleViolationException;

@Service
@RequiredArgsConstructor
public class AdminReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationTimeValidationService reservationTimeValidationService;

    public ReservationTimeServiceResponse create(CreateReservationTimeServiceRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.save(request.toReservationTime());
        return ReservationTimeServiceResponse.withoutBook(reservationTime);
    }

    public List<ReservationTimeServiceResponse> getAll() {
        List<ReservationTime> allTimes = reservationTimeRepository.getAll();
        return allTimes.stream()
                .map(ReservationTimeServiceResponse::withoutBook)
                .toList();
    }

    public void delete(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.getById(id);
        try {
            reservationTimeValidationService.validateNotInUse(id);
        } catch (ReservationException e) {
            throw new BusinessRuleViolationException(e.getMessage(), e);
        }

        reservationTimeRepository.remove(reservationTime);
    }
}
