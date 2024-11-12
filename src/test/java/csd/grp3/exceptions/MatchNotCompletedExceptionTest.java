package csd.grp3.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import csd.grp3.exception.MatchNotCompletedException;

public class MatchNotCompletedExceptionTest {

    @Test
    public void testMatchNotCompletedExceptionMessage() {
        // Arrange
        Long matchId = 123L;
        String expectedMessage = "Match " + matchId + " not completed yet.";

        // Act
        MatchNotCompletedException exception = new MatchNotCompletedException(matchId);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}

