package roomescape.service;

import static roomescape.exception.ExceptionType.DUPLICATE_RESERVATION;
import static roomescape.exception.ExceptionType.PAST_TIME;
import static roomescape.exception.ExceptionType.RESERVATION_TIME_NOT_FOUND;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    //ToDo 테스트 작성
    public ReservationResponse save(ReservationRequest reservationRequest) {
        //TODO 변수명
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new RoomescapeException(RESERVATION_TIME_NOT_FOUND));

        Reservation beforeSave = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                //TODO : 커스텀 예외 사용할지 고민해보기
                reservationTime
        );
        boolean isDuplicate = reservationRepository.findAll()
                .stream()
                .anyMatch(reservation -> mapToLocalDateTime(reservation).equals(mapToLocalDateTime(beforeSave)));
        if (isDuplicate) {
            throw new RoomescapeException(DUPLICATE_RESERVATION);
        }

        //todo 테스트
        if (isBefore(beforeSave)) {
            throw new RoomescapeException(PAST_TIME);
        }

        Reservation saved = reservationRepository.save(beforeSave);
        return toResponse(saved);
    }

    private static boolean isBefore(Reservation beforeSave) {
        return LocalDateTime.of(beforeSave.getDate(), beforeSave.getTime())
                .isBefore(LocalDateTime.now());
    }

    private LocalDateTime mapToLocalDateTime(Reservation reservation) {
        LocalDate date = reservation.getDate();
        LocalTime time = reservation.getReservationTime().getStartAt();
        return LocalDateTime.of(date, time);
    }

    private ReservationResponse toResponse(Reservation reservation) {
        ReservationTime reservationTime = reservation.getReservationTime();
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(reservationTime.getId(),
                reservation.getTime());
        return new ReservationResponse(reservation.getId(),
                reservation.getName(), reservation.getDate(), reservationTimeResponse);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(long id) {
        reservationRepository.delete(id);
    }
}
