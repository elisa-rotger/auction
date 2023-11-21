package com.weareadaptive.auctionhouse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StringUtilTest {

  private static Stream<Arguments> testEmptyArguments() {
    return Stream.of(
        Arguments.of(null, true),
        Arguments.of("", true),
        Arguments.of("  ", true),
        Arguments.of("test", false)
    );
  }

  private static Stream<Arguments> testAlphanumericArguments() {
    return Stream.of(
            Arguments.of("testing", true),
            Arguments.of("testing675923", true),
            Arguments.of("49203'4", true),
            Arguments.of("    ", false),
            Arguments.of("asa  asad", false),
            Arguments.of("asl&&2", false)

    );
  }

  @ParameterizedTest(name = "{0} should return {1}")
  @MethodSource("testEmptyArguments")
  public void shouldTestStringIsNotNullOrBlank(String input, boolean expectedResult) {
    assertEquals(expectedResult, StringUtil.isNullOrEmpty(input));
  }

  @MethodSource("testAlphanumericArguments")
  public void shouldTestStringIsAlphanumeric(String input, boolean expectedResult) {
    assertEquals(expectedResult, StringUtil.isAlphanumeric(input));
  }
}
