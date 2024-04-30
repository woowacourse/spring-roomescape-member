package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
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
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(reservationRequest.timeId());

        Reservation beforeSave = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                //TODO : 커스텀 예외 사용할지 고민해보기
                reservationTime.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."))
        );
        boolean isDuplicate = reservationRepository.findAll()
                .stream()
                .anyMatch(reservation -> mapToLocalDateTime(reservation).equals(mapToLocalDateTime(beforeSave)));
        if (isDuplicate) {
            throw new IllegalArgumentException("동일한 날짜와 시간에 이미 예약이 존재합니다.");
        }
        Reservation saved = reservationRepository.save(beforeSave);
        return toResponse(saved);
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
