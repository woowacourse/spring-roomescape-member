package roomescape.support;

import java.util.List;

public class SqlFixture {

    public static final String INSERT_MEMBERS = """
        INSERT INTO MEMBER (NAME, EMAIL, PASSWORD, ROLE)
        VALUES
            ('어드민1', 'admin1@email.com', 'adminpw1', 'ADMIN'),
            ('어드민2', 'admin2@email.com', 'adminpw2', 'ADMIN'),
            ('유저1', 'user1@email.com', 'userpw1', 'USER'),
            ('유저2', 'user2email.com', 'userpw2', 'USER');
    """;

    public static final String INSERT_RESERVATION_TIMES = """
        INSERT INTO RESERVATION_TIME (START_AT)
        VALUES
            ('10:00'),
            ('12:00'),
            ('14:00'),
            ('16:00'),
            ('18:00'),
            ('20:00');
    """;

    public static final String INSERT_THEMES = """
        INSERT INTO THEME (NAME, DESCRIPTION, THUMBNAIL)
        VALUES
            ('레벨1 탈출', '우테코 레벨1을 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
            ('레벨2 탈출', '우테코 레벨2를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
            ('레벨3 탈출', '우테코 레벨3을 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
            ('레벨4 탈출', '우테코 레벨4를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
            ('레벨5 탈출', '우테코 레벨5를 탈출하는 내용입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');
    """;

    public static final String INSERT_RESERVATIONS = """
       INSERT INTO RESERVATION (MEMBER_ID, DATE, TIME_ID, THEME_ID)
       VALUES
           (1, CURRENT_DATE, 1, 2),
           (2, CURRENT_DATE, 2, 2),
           (3, CURRENT_DATE, 3, 2),
           (1, CURRENT_DATE, 4, 1),
           (2, CURRENT_DATE, 5, 1),
           (3, CURRENT_DATE - 1, 3, 3),
           (1, CURRENT_DATE - 1, 4, 4),
           (2, CURRENT_DATE - 1, 5, 5);
    """;

    public static final List<String> INSERT_ALL = List.of(
            INSERT_MEMBERS,
            INSERT_RESERVATION_TIMES,
            INSERT_THEMES,
            INSERT_RESERVATIONS
    );
}
