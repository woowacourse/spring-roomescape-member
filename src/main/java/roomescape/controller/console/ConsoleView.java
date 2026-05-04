package roomescape.controller.console;

import java.util.List;
import java.util.Scanner;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;

public final class ConsoleView {

    private static final Scanner scanner = new Scanner(System.in);

    public static void printMenu() {
        System.out.println("\n--- 방탈출 관리 시스템 ---");
        System.out.println("[예약 관리] 1: 목록, 2: 추가, 3: 삭제");
        System.out.println("[시간 관리] 4: 목록, 5: 추가, 6: 삭제");
        System.out.println("[기타] q: 종료");
    }

    public static void printTimes(List<ReservationTimeResult> times) {
        System.out.println("\n[현재 등록된 시간 목록]");
        if (times.isEmpty()) {
            System.out.println("등록된 시간이 없습니다.");
        }
        times.forEach(time ->
                System.out.printf("ID: %d | 시작 시간: %s\n", time.id(), time.startAt())
        );
    }

    public static String readMenu() {
        printMenu();
        System.out.print("> 메뉴를 선택하세요: ");
        return scanner.nextLine();
    }

    public static void printReservations(List<ReservationResult> reservations) {
        System.out.println("\n[현재 예약 목록]");
        if (reservations.isEmpty()) {
            System.out.println("예약이 없습니다.");
        }
        reservations.forEach(res ->
                System.out.printf("ID: %d | 이름: %s | 날짜: %s | 시간: %s\n",
                        res.id(), res.name(), res.date(), res.time().startAt())
        );
    }

    public static String readInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printError(String message) {
        System.out.println();
        System.err.println(message);
    }
}
