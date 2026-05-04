package roomescape.time.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.validator.ReservationTimeValidator;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.presentation.dto.ReservationTimeRequest;
import roomescape.time.presentation.dto.ReservationTimeResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeValidator reservationTimeValidator;
    private final ReservationTimeRepository repositoryTimeRepository;

    @Transactional(readOnly = true)
    public List<ReservationTimeResponse> getReservationTimes() {
        return repositoryTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime time = ReservationTimeRequest.toEntity(request);
        return ReservationTimeResponse.from(repositoryTimeRepository.save(time));
    }

    public void deleteReservationTime(Long id) {
        reservationTimeValidator.validateDeletable(id);
        repositoryTimeRepository.deleteById(id);
    }
}
