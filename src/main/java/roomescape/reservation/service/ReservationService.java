package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.request.ReservationRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;
import roomescape.theme.entity.ThemeEntity;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.ReservationTimeEntity;
import roomescape.time.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponse> getAllReservation() {
        return reservationRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ReservationResponse convertToResponse(ReservationEntity reservation) {
        final Long themeId = reservation.getThemeId();
        ThemeEntity themeEntity = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(String.format("%d 식별자의 테마는 존재하지 않습니다.", themeId)));
        return ReservationResponse.from(reservation, themeEntity);
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTimeEntity timeEntity = timeRepository.findById(request.timeId())
                .orElseThrow(() -> new NotFoundException(String.format("%d 식별자의 예약 시간은 존재하지 않습니다.", request.timeId())));
        ThemeEntity themeEntity = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundException(String.format("%d 식별자의 테마는 존재하지 않습니다.", request.themeId())));

        ReservationEntity newReservation = request.toEntity(timeEntity);
        validateDuplicated(newReservation);

        ReservationEntity saved = reservationRepository.save(newReservation);
        return ReservationResponse.from(saved, themeEntity);
    }

    private void validateDuplicated(ReservationEntity newReservation) {
        if (isExistDuplicatedWith(newReservation)) {
            throw new ConflictException("해당 날짜에는 이미 예약이 존재합니다.");
        }
    }

    private boolean isExistDuplicatedWith(ReservationEntity target) {
        return reservationRepository.findDuplicatedWith(target).isPresent();
    }

    public void deleteReservation(final Long id) {
        final boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException(String.format("%d는 존재하지 않는 예약 식별자입니다.", id));
        }
    }
}
