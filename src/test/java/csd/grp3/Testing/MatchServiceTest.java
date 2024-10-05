package csd.grp3.Testing;

import csd.grp3.round.Round;
import csd.grp3.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import csd.grp3.match.*;

public class MatchServiceTest {

    @InjectMocks
    private MatchServiceImpl matchService;

    @Mock
    private MatchRepository matchRepository;

    private Match match;
    private Round round;
    private Player player;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        round = new Round();
        round.setId(1L);

        player = new Player();
        player.setUsername("playerUser");
        player.setPassword("password");
        player.setAuthorities("ROLE_PLAYER");

        match = new Match();
        match.setId(1L);
        match.setRound(round);
        match.setBYE(false);
    }

    @Test
    void testGetRoundMatches() {
        List<Match> matchesList = new ArrayList<>();
        matchesList.add(match);
        
        when(matchRepository.findByRound(round)).thenReturn(matchesList);

        List<Match> result = matchService.getRoundMatches(round);

        assertEquals(1, result.size());
        assertEquals(match, result.get(0));
        verify(matchRepository, times(1)).findByRound(round);
    }

    @Test
    void testGetMatch() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        Match result = matchService.getMatch(1L);

        assertNotNull(result);
        assertEquals(match.getId(), result.getId());
        verify(matchRepository, times(1)).findById(1L);
    }

    @Test
    void testAddMatch() {
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        Match result = matchService.addMatch(match);

        assertEquals(match, result);
        verify(matchRepository, times(1)).save(match);
    }

    @Test
    void testUpdateMatch() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        Match newMatchInfo = new Match();
        newMatchInfo.setResult(1);
        newMatchInfo.setBYE(true);

        Match updatedMatch = matchService.updateMatch(1L, newMatchInfo);

        assertNotNull(updatedMatch);
        assertEquals(newMatchInfo.getResult(), updatedMatch.getResult());
        assertTrue(updatedMatch.isBYE());
        verify(matchRepository, times(1)).findById(1L);
        verify(matchRepository, times(1)).save(updatedMatch);
    }

    @Test
    void testDeleteMatch() {
        doNothing().when(matchRepository).deleteById(1L);

        assertDoesNotThrow(() -> matchService.deleteMatch(1L));
        verify(matchRepository, times(1)).deleteById(1L);
    }
}