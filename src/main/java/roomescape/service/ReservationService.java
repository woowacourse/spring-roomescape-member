package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ServiceReservationRequest;
import roomescape.service.dto.ServiceReservationResponse;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ServiceReservationResponse create(ServiceReservationRequest requestDto) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.read(requestDto.timeId());
        if (reservationTime.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
        }

        Optional<Theme> theme = themeRepository.read(requestDto.themeId());
        if (theme.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_THEME);
        }

        boolean existReservation = reservationRepository.existByDateAndTimeIdAndThemeId(requestDto.date(),
                requestDto.timeId(),
                requestDto.themeId());
        if (existReservation) {
            throw new CustomException(ErrorCode.DUPLICATED_RESERVATION);
        }

        Reservation reservationWithoutId = requestDto.toEntity(reservationTime.get(), theme.get());
        Reservation reservation = reservationRepository.create(reservationWithoutId);

        return ServiceReservationResponse.from(reservation);
    }

    public List<ServiceReservationResponse> readAll() {
        List<Reservation> reservations = reservationRepository.readAll();
        return reservations.stream()
                .map(ServiceReservationResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
