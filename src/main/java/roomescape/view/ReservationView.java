package roomescape.view;

import org.springframework.stereotype.Component;
import roomescape.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Component
public class ReservationView {
    private final Scanner scanner = new Scanner(System.in);

    public String readName() {
        System.out.println("[INFO] 방탈출 예약을 진행합니다. 예약자 이름을 입력해주세요.");
        return scanner.nextLine();
    }

    public LocalDate readDate() {
        System.out.println("[INFO] 예약할 날짜를 선택해주세요. (ex. 2023-01-01)");
        try {
            return LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("[ERROR] 날짜를 올바르게 입력해주세요.");
        }
    }

    public void printSuccessfullyAdded() {
        System.out.println("[INFO] 예약이 추가되었습니다.");
    }

    public long readIndexToDelete(final List<ReservationResponse> reservationResponses) {
        System.out.println("[INFO] 삭제할 방탈출 예약 번호를 선택해주세요.");
        printReservations(reservationResponses);
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 예약 번호 입력입니다.");
        }
    }

    public void printSuccessfullyDeleted() {
        System.out.println("[INFO] 예약이 삭제되었습니다.");
    }

    public void printReservations(final List<ReservationResponse> reservations) {
        System.out.println("[INFO] 방탈출 예약 목록입니다.");
        System.out.println("번호  이름    날짜      시간");
        for (ReservationResponse reservationResponse : reservations) {
            printReservation(reservationResponse.id(), reservationResponse);
        }
    }

    private void printReservation(final long id, final ReservationResponse reservationResponse) {
        System.out.printf(
                "%d.  %s  %s  %s%n",
                id,
                reservationResponse.name(),
                reservationResponse.date().toString(),
                reservationResponse.time().getStartAt().toString()
        );
    }

    public void printHasNotAnyReservationTime() {
        System.out.printf("[WARN] 방탈출 예약 가능한 시간이 없기 때문에 메뉴로 돌아갑니다.%n");
    }

    public void printHasNotAnyReservation() {
        System.out.printf("[WARN] 방탈출 예약 내역이 없기 때문에 메뉴로 돌아갑니다.%n");
    }
}
