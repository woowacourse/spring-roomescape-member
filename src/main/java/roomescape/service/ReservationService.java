package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.response.MemberReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.service.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAllReservations();
    }

    public Reservation saveReservation(ReservationDto reservationDto) {
        String name = reservationDto.getName();
        LocalDate date = reservationDto.getDate();
        long timeId = reservationDto.getTimeId();
        long themeId = reservationDto.getThemeId();
        ReservationTime time = reservationRepository.findReservationTimeById(timeId)
                .orElseThrow(() -> new BadRequestException("[ERROR] 데이터가 저장되지 않습니다."));
        Theme theme = reservationRepository.findThemeById(themeId)
                .orElseThrow(() -> new BadRequestException("[ERROR] 데이터가 저장되지 않습니다."));

        validate(date, time);
        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationRepository.saveReservation(reservation);
    }

    private void validate(LocalDate date, ReservationTime time) {
        validateDateTime(date, time);
        validateDuplication(date, time.getId());
    }

    private void validateDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt()).truncatedTo(ChronoUnit.SECONDS);
        if (dateTime.isBefore(now)) {
            throw new BadRequestException("[ERROR] 현재 이전 예약은 할 수 없습니다.");
        }
    }

    private void validateDuplication(LocalDate date, long timeId) {
        boolean isExist = reservationRepository.isExistReservationByDateAndTimeId(date, timeId);
        if (isExist) {
            throw new DuplicatedException("[ERROR] 중복되는 예약은 추가할 수 없습니다.");
        }
    }

    public void deleteReservation(long id) {
        validateExistence(id);
        reservationRepository.deleteReservationById(id);
    }

    private void validateExistence(long id) {
        boolean isNotExist = !reservationRepository.isExistReservationById(id);
        if (isNotExist) {
            throw new NotFoundException("[ERROR] 존재하지 않는 예약입니다.");
        }
    }

    public List<MemberReservationTimeResponse> findReservationTimesInformation(LocalDate date, long themeId) {
        List<ReservationTime> bookedTimes = reservationRepository.findReservationTimeBooked(date, themeId);
        List<ReservationTime> notBookedTimes = reservationRepository.findReservationTimeNotBooked(date, themeId);
        List<MemberReservationTimeResponse> bookedResponse = mapToResponse(bookedTimes, true);
        List<MemberReservationTimeResponse> notBookedResponse = mapToResponse(notBookedTimes, false);
        return concat(notBookedResponse, bookedResponse);
    }

    private List<MemberReservationTimeResponse> mapToResponse(List<ReservationTime> times, boolean isBooked) {
        return times.stream()
                .map(time -> new MemberReservationTimeResponse(time.getId(), time.getStartAt(), isBooked))
                .toList();
    }

    private List<MemberReservationTimeResponse> concat(List<MemberReservationTimeResponse> notBookedTimes,
                                                       List<MemberReservationTimeResponse> bookedTimes) {
        return Stream.concat(notBookedTimes.stream(), bookedTimes.stream()).toList();
    }
}
