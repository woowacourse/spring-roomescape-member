package roomescape.view;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import roomescape.dto.ReservationTimeResponse;

@Component
public class ReservationTimeView {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d+:\\d+$");

    public LocalTime readStartAt() {
        System.out.println("[INFO] 방탈출 예약이 가능한 시간을 추가해주세요. (ex. 23:30)");
        String rawReservationTime = SCANNER.nextLine();
        TIME_PATTERN.matcher(rawReservationTime);

        try {
            return LocalTime.parse(rawReservationTime);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("[ERROR] 방탈출 예약 가능 시간을 올바르게 입력해주세요.");
        }
    }

    public void printSuccessfullyAdded() {
        System.out.println("[INFO] 예약 가능 시간이 추가되었습니다.");
    }

    public static long readIndexToDelete(final List<ReservationTimeResponse> reservationTimeResponses) {
        System.out.println("[INFO] 삭제할 방탈출 예약 가능 시간의 번호를 선택해주세요.");
        return readIndex(reservationTimeResponses);
    }

    public static long readIndexToReserve(final List<ReservationTimeResponse> reservationTimeResponses) {
        System.out.println("[INFO] 방탈출 예약 가능 시간의 번호를 선택해주세요.");
        return readIndex(reservationTimeResponses);
    }

    private static long readIndex(final List<ReservationTimeResponse> reservationTimeResponses) {
        printReservationTimes(reservationTimeResponses);
        try {
            return Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("[ERROR] 예약 가능 시간의 번호를 올바르게 입력해주세요.");
        }
    }

    public void printSuccessfullyDeleted() {
        System.out.println("[INFO] 예약 가능 시간이 삭제되었습니다.");
    }

    public static void printReservationTimes(final List<ReservationTimeResponse> reservationTimeResponses) {
        System.out.println("[INFO] 방탈출 예약이 가능한 시간의 목록입니다.");
        System.out.println("번호  시작 시간");
        for (ReservationTimeResponse reservationTimeResponse : reservationTimeResponses) {
            printReservationTime(reservationTimeResponse.timeId(), reservationTimeResponse);
        }
    }

    private static void printReservationTime(final long id, final ReservationTimeResponse response) {
        System.out.printf(" %d  %s%n", id, response.startAt().toString());
    }

    public void printHasNotAnyReservationTimeToDelete() {
        System.out.printf("[WARN] 삭제할 방탈출 예약 시간이 없기 때문에 메뉴로 돌아갑니다.%n");
    }
}
