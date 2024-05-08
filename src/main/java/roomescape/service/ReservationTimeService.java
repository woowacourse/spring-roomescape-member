package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.FindTimeAndAvailabilityDto;
import roomescape.service.dto.SaveReservationTimeDto;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
        ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime save(SaveReservationTimeDto dto) {
        LocalTime parsedTime = parseTime(dto.startAt());
        validateDuplication(parsedTime);
        ReservationTime newReservationTime = new ReservationTime(parsedTime);

        return reservationTimeRepository.save(newReservationTime);
    }

    private LocalTime parseTime(String rawTime) {
        try {
            return LocalTime.parse(rawTime);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new RoomescapeException("잘못된 시간 형식입니다.");
        }
    }

    private void validateDuplication(LocalTime parsedTime) {
        if (reservationTimeRepository.isStartTimeExists(parsedTime)) {
            throw new RoomescapeException("이미 존재하는 시간은 추가할 수 없습니다.");
        }
    }

    public int delete(Long id) {
        if (reservationRepository.isTimeIdUsed(id)) {
            throw new RoomescapeException("해당 시간을 사용하는 예약이 존재하여 삭제할 수 없습니다.");
        }
        return reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public List<FindTimeAndAvailabilityDto> findAllWithBookAvailability(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        List<ReservationTime> reservedTimes = reservations.stream()
            .map(Reservation::getTime)
            .toList();

        return findAll().stream()
            .map(time -> new FindTimeAndAvailabilityDto(
                    time.getId(),
                    time.getStartAt(),
                    reservedTimes.contains(time)
                )
            ).toList();
    }
}
