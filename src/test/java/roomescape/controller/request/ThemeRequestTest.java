package roomescape.controller.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThemeRequestTest {

    @DisplayName("요청된 데이터의 이름이 null 혹은 비어있는 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n", "\t"})
    void should_throw_exception_when_invalid_name(String name) {
        assertThatThrownBy(() -> new ThemeRequest(name, "d", "t"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
    }

    @DisplayName("요청된 데이터의 설명이 null 혹은 비어있는 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n", "\t"})
    void should_throw_exception_when_invalid_description(String description) {
        assertThatThrownBy(() -> new ThemeRequest("n", description, "t"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
    }

    @DisplayName("요청된 데이터의 썸네일이 null 혹은 비어있는 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\n", "\t"})
    void should_throw_exception_when_invalid_thumbnail(String thumbnail) {
        assertThatThrownBy(() -> new ThemeRequest("n", "d", thumbnail))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
    }

    @DisplayName("요청된 데이터가 유효한 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"nnn,ddd,ttt", "111,222,333", " n , d   , t"})
    void should_not_throw_exception_when_valid_data(String name, String description, String thumbnail) {
        assertThatCode(() -> new ThemeRequest(name, description, thumbnail))
                .doesNotThrowAnyException();
    }
}
