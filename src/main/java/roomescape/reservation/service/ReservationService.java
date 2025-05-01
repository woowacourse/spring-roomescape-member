package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.ReservationThemeEntity;
import roomescape.theme.repository.ReservationThemeRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.reservation.service.dto.request.ReservationRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.time.entity.ReservationTimeEntity;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;
    private final ReservationThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository timeRepository,
            ReservationThemeRepository themeRepository
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
        ReservationThemeEntity themeEntity = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마 입니다."));
        return ReservationResponse.from(reservation, themeEntity);
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTimeEntity timeEntity = timeRepository.findById(request.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간 입니다."));
        ReservationThemeEntity themeEntity = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마 입니다."));

        ReservationEntity newReservation = request.toEntity(timeEntity);
        validateDateTime(newReservation);
        validateDuplicated(newReservation);

        ReservationEntity saved = reservationRepository.save(newReservation);
        return ReservationResponse.from(saved, themeEntity);
    }

    private void validateDateTime(ReservationEntity reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = reservation.getDateTime();
        if (reservationDateTime.isBefore(now)) {
            throw new BadRequestException("과거 날짜/시간의 예약은 생성할 수 없습니다.");
        }
    }

    private void validateDuplicated(ReservationEntity newReservation) {
        if (isExistDuplicatedWith(newReservation)) {
            throw new ConflictException("해당 날짜에는 이미 예약이 존재합니다.");
        }
    }

    private boolean isExistDuplicatedWith(ReservationEntity target) {
        List<ReservationEntity> reservations = reservationRepository.findAll();
        return reservations.stream().anyMatch(reservation -> reservation.isDuplicatedWith(target));
    }

    public void deleteReservation(final Long id) {
        final boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }
}
