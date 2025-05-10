package roomescape.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.global.error.exception.BadRequestException;
import roomescape.global.error.exception.ConflictException;
import roomescape.global.error.exception.NotFoundException;
import roomescape.reservation.dto.request.ReservationRequest.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse.ReservationCreateResponse;
import roomescape.reservation.dto.response.ReservationResponse.ReservationReadResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationCreateResponse createReservation(ReservationCreateRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간 입니다."));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마 입니다."));

        Reservation newReservation = request.toEntity(time);
        validateDateTime(newReservation);
        validateDuplicated(newReservation);

        Reservation saved = reservationRepository.save(newReservation);
        return ReservationCreateResponse.from(saved, theme);
    }

    public List<ReservationReadResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservation -> {
                    Theme theme = themeRepository.findById(reservation.getThemeId())
                            .orElseThrow(() -> new NotFoundException("존재하지 않는 테마 입니다."));
                    return ReservationReadResponse.from(reservation, theme);
                })
                .toList();
    }

    public void deleteReservation(final Long id) {
        boolean deleted = reservationRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }

    private void validateDateTime(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = reservation.getDateTime();
        if (reservationDateTime.isBefore(now)) {
            throw new BadRequestException("과거 날짜/시간의 예약은 생성할 수 없습니다.");
        }
    }

    private void validateDuplicated(Reservation reservation) {
        if (reservationRepository.existsByDateAndTimeId(reservation.getDate(), reservation.getTime().getId())) {
            throw new ConflictException("해당 날짜에는 이미 예약이 존재합니다.");
        }
    }
}
