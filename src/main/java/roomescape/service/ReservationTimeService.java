package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomConflictException;
import roomescape.exception.CustomNotFoundException;
import roomescape.exception.CustomUnprocessableEntityException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.request.ServiceReservationTimeCreateRequest;
import roomescape.service.dto.response.ServiceReservationTimeAvailabilityResponse;
import roomescape.service.dto.response.ServiceReservationTimeResponse;

@Service
@Transactional(readOnly = true)
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ServiceReservationTimeResponse create(ServiceReservationTimeCreateRequest request) {
        if (reservationTimeRepository.existByStartAt(request.startAt())) {
            throw new CustomConflictException(ErrorCode.DUPLICATED_RESERVATION_TIME);
        }

        ReservationTime reservationTime = reservationTimeRepository.create(request.toEntity());
        return ServiceReservationTimeResponse.from(reservationTime);
    }

    public List<ServiceReservationTimeResponse> readAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.readAll();
        return reservationTimes.stream()
                .map(ServiceReservationTimeResponse::from)
                .toList();
    }

    public List<ServiceReservationTimeAvailabilityResponse> readAvailabilityByDateAndTheme(
            LocalDate date, Long themeId) {
        Optional<Theme> theme = themeRepository.read(themeId);
        if (theme.isEmpty()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_THEME);
        }
        if (date.isBefore(LocalDate.now())) {
            throw new CustomUnprocessableEntityException(ErrorCode.PAST_RESERVATION_TIME_READ);
        }

        List<ReservationTime> allReservationTimes = reservationTimeRepository.readAll();
        List<Long> reservedTimeIdByDateAndTheme = reservationTimeRepository.reservedTimeIdByDateAndTheme(date, themeId);

        return allReservationTimes.stream()
                .map(reservationTime -> {
                    if (reservedTimeIdByDateAndTheme.contains(reservationTime.getId())) {
                        return ServiceReservationTimeAvailabilityResponse.from(reservationTime, false);
                    }
                    return ServiceReservationTimeAvailabilityResponse.from(reservationTime, true);
                }).toList();
    }

    @Transactional
    public void delete(Long id) {
        if (reservationRepository.existByTimeId(id)) {
            throw new CustomConflictException(ErrorCode.REFERENCED_TIME);
        }
        reservationTimeRepository.delete(id);
    }
}
