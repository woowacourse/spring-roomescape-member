package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomConflictException;
import roomescape.exception.CustomNotFoundException;
import roomescape.exception.CustomUnprocessableEntityException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ServiceReservationCreateRequest;
import roomescape.service.dto.request.ServiceReservationUpdateRequest;
import roomescape.service.dto.response.ServiceReservationResponse;

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
    public ServiceReservationResponse create(ServiceReservationCreateRequest request) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.read(request.timeId());
        if (reservationTime.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
        }

        Optional<Theme> theme = themeRepository.read(request.themeId());
        if (theme.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_THEME);
        }

        boolean existReservation = reservationRepository.existByDateAndTimeIdAndThemeId(request.date(),
                request.timeId(),
                request.themeId());
        if (existReservation) {
            throw new CustomConflictException(ErrorCode.DUPLICATED_RESERVATION);
        }

        Reservation reservationWithoutId = request.toEntity(reservationTime.get(), theme.get());
        if (reservationWithoutId.isPast(LocalDateTime.now())) {
            throw new CustomUnprocessableEntityException(ErrorCode.NOT_ALLOW_PAST_TIME_RESERVATION_CREATE);
        }

        Reservation reservation = reservationRepository.create(reservationWithoutId);

        return ServiceReservationResponse.from(reservation);
    }

    public List<ServiceReservationResponse> readByName(String name) {
        List<Reservation> reservations = reservationRepository.readByName(name);

        return reservations.stream()
                .map(ServiceReservationResponse::from)
                .toList();
    }

    public List<ServiceReservationResponse> readAll() {
        List<Reservation> reservations = reservationRepository.readAll();

        return reservations.stream()
                .map(ServiceReservationResponse::from)
                .toList();
    }

    public ServiceReservationResponse update(Long id, ServiceReservationUpdateRequest request) {
        Optional<Reservation> beforeReservation = reservationRepository.readById(id);
        if (beforeReservation.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_RESERVATION);
        }
        if (beforeReservation.get().isPast(LocalDateTime.now())) {
            throw new CustomUnprocessableEntityException(ErrorCode.NOT_ALLOW_PAST_TIME_RESERVATION_UPDATE);
        }

        Optional<ReservationTime> reservationTime = reservationTimeRepository.read(request.timeId());
        if (reservationTime.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
        }

        Reservation newReservation = request.toEntity(beforeReservation.get(), reservationTime.get());
        if (newReservation.isPast(LocalDateTime.now())) {
            throw new CustomUnprocessableEntityException(ErrorCode.NOT_ALLOW_PAST_TIME_RESERVATION_CREATE);
        }

        if (reservationRepository.existByDateAndTimeIdAndThemeId(newReservation.getDate(),
                newReservation.getTime().getId(), newReservation.getTheme().getId())) {
            throw new CustomConflictException(ErrorCode.DUPLICATED_RESERVATION);
        }

        reservationRepository.update(id, request.date(), request.timeId());

        return ServiceReservationResponse.from(newReservation);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Reservation> reservation = reservationRepository.readById(id);
        if (reservation.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_RESERVATION);
        }
        if (reservation.get().isPast(LocalDateTime.now())) {
            throw new CustomUnprocessableEntityException(ErrorCode.NOT_ALLOW_PAST_TIME_RESERVATION_DELETE);
        }

        reservationRepository.delete(id);
    }
}
