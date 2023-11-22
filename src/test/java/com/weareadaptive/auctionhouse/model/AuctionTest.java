package com.weareadaptive.auctionhouse.model;

import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuctionTest {
    private static Stream<Arguments> createAuctionArguments() {
        return Stream.of(
                // Null / empty owner
                Arguments.of("owner",
                        (Executable) () -> new Auction(1, null, "pp", 20.5, 5)),
                Arguments.of("owner",
                        (Executable) () -> new Auction(1, "", "pp", 20.5, 5)),
                // Null / empty symbol
                Arguments.of("symbol",
                        (Executable) () -> new Auction(1, "username", null, 20.5, 5)),
                Arguments.of("symbol",
                        (Executable) () -> new Auction(1, "username", "", 20.5, 5)),
                // 0 / negative minimum price
                Arguments.of("minPrice",
                        (Executable) () -> new Auction(1, "username", "pp", 0, 5)),
                Arguments.of("minPrice",
                        (Executable) () -> new Auction(1, "username", "pp", -36, 5)),
                // 0 / negative available quantity
                Arguments.of("availableQty",
                        (Executable) () -> new Auction(1, "username", "pp", 20.5, 0)),
                Arguments.of("availableQty",
                        (Executable) () -> new Auction(1, "username", "pp", 20.5, -6))
        );
    }

    @ParameterizedTest(name = "Create auction should throw exception when {0} is invalid")
    @MethodSource("createAuctionArguments")
    public void createAuctionShouldThrowWhenInvalidProperty(String propertyName, Executable auctionExecutable) {
        var exception = assertThrows(BusinessException.class, auctionExecutable);
        assertTrue(exception.getMessage().contains(propertyName));
    }
}
