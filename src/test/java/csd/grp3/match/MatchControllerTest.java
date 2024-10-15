package csd.grp3.match;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import csd.grp3.round.Round;
import csd.grp3.tournament.Tournament;
import csd.grp3.user.User;

@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @Test
    public void testUpdateMatches() {
        Tournament tournament = new Tournament("ChessTournament", 800, 1200, LocalDateTime.now().plusDays(1), 5, 5);

        Round round1 = new Round(tournament);
        Round round2 = new Round(tournament);

        User whitePlayer1 = new User("Joel", "icecream");
        User blackPlayer1 = new User("Teck Ren", "lollipop");

        User whitePlayer2 = new User("Daniel", "cheesecake");
        User blackPlayer2 = new User("Justin", "brownie");

        Match match1 = new Match(whitePlayer1, blackPlayer1, round1);
        Match match2 = new Match(whitePlayer2, blackPlayer2, round2);

        List<Match> inputMatches = Arrays.asList(match1, match2);

        Match updatedMatch1 = new Match(whitePlayer1, blackPlayer1, round1);
        updatedMatch1.setResult(1);

        Match updatedMatch2 = new Match(whitePlayer2, blackPlayer2, round2);
        updatedMatch2.setResult(0.5);

        List<Match> expectedMatches = Arrays.asList(updatedMatch1, updatedMatch2);

        when(matchService.updateMatch(match1.getId(), match1)).thenReturn(updatedMatch1);
        when(matchService.updateMatch(match2.getId(), match2)).thenReturn(updatedMatch2);

        List<Match> actualMatches = matchController.updateMatches(inputMatches);

        assertEquals(expectedMatches, actualMatches, "The updated matches should match the list");

        verify(matchService).updateMatch(match1.getId(), match1);
        verify(matchService).updateMatch(match2.getId(), match2);
    }
}
