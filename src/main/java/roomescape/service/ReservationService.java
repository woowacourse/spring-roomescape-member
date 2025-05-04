package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreateRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponseDto createReservation(ReservationCreateRequestDto dto) {
        Reservation requestReservation = createRequestReservation(dto);

        validateRequestReservation(requestReservation);

        Reservation newReservation = reservationRepository.save(requestReservation)
                .orElseThrow(() -> new IllegalStateException("[ERROR] 알 수 없는 오류로 인해 예약 생성을 실패하였습니다."));

        return ReservationResponseDto.from(newReservation, newReservation.getTime(), newReservation.getTheme());
    }

    private Reservation createRequestReservation(ReservationCreateRequestDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 예약 시간을 찾을 수 없습니다. id : " + dto.timeId()));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new NotFoundException("[ERROR] 테마를 찾을 수 없습니다. id : " + dto.themeId()));

        return dto.createWithoutId(reservationTime, theme);
    }

    private void validateRequestReservation(Reservation requestReservation) {
        if (requestReservation.isBeforeCurrentDateTime()) {
            throw new InvalidRequestException("[ERROR] 현 시점 이후의 날짜와 시간을 선택해주세요.");
        }

        ReservationTime time = requestReservation.getTime();
        Theme theme = requestReservation.getTheme();
        List<Reservation> reservations = reservationRepository.findByDateTimeTheme(requestReservation.getDate(), time.getStartAt(), theme.getId());
        if (!reservations.isEmpty()) {
            throw new DuplicateContentException("[ERROR] 이미 예약이 존재합니다. 다른 예약 일정을 선택해주세요.");
        }
    }

    public List<ReservationResponseDto> findAllReservationResponses() {
        List<Reservation> allReservations = reservationRepository.findAll();

        return allReservations.stream()
                .map(reservation -> ReservationResponseDto.from(reservation, reservation.getTime(), reservation.getTheme()))
                .toList();
    }

    public void deleteReservation(Long id) {
        int deletedReservationCount = reservationRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new NotFoundException("[ERROR] 등록된 예약번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
