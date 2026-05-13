package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ServiceReservationTimeAvailabilityResponse;
import roomescape.service.dto.ServiceReservationTimeRequest;
import roomescape.service.dto.ServiceReservationTimeResponse;

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
    public ServiceReservationTimeResponse create(ServiceReservationTimeRequest requestDto) {
        ReservationTime reservationTime = reservationTimeRepository.create(requestDto.toEntity());
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
        themeRepository.read(themeId);

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
            throw new CustomException(ErrorCode.REFERENCED_TIME);
        }
        reservationTimeRepository.delete(id);
    }
}
