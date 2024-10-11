package csd.grp3.testing;

import csd.grp3.match.Match;
import csd.grp3.match.MatchNotFoundException;
import csd.grp3.match.MatchRepository;
import csd.grp3.match.MatchServiceImpl;
import csd.grp3.round.Round;
import csd.grp3.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matches;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void addMatch() {
        // arrange
        Match match = new Match();

        // act
        // mock the save operation
        when(matches.save(any(Match.class))).thenReturn(match);

        Match returnedMatch = matchService.addMatch(match);

        // assert
        assertEquals(match,returnedMatch);
        verify(matches).save(match);
    }

    @Test
    void getMatch_validId() {
        // arrange
        Match match = new Match();
        matchService.addMatch(match);

        // mock the find by id operation
        when(matches.findById(match.getId())).thenReturn(Optional.of(match));

        // act
        Match returnedMatch = matchService.getMatch(match.getId());

        //assert
        assertEquals(match,returnedMatch);
        verify(matches).findById(match.getId());
    }

    @Test
    void getMatch_invalidId() {
        // arrange
        long invalidID = 0L;

        // mock the find by id operation
        when(matches.findById(invalidID)).thenReturn(Optional.empty());

        // act

        //assert
        assertThrows(MatchNotFoundException.class, () -> {
            matchService.getMatch(invalidID);
        });

        verify(matches).findById(invalidID);
    }

    @Test
    void updateMatch_validId() {
        // arrange
        Match match = new Match();
        matchService.addMatch(match);
        match.setResult(1);
        match.setBYE(true);

        // mock the find by id operation
        when(matches.findById(match.getId())).thenReturn(Optional.of(match));
        // mock the save operation
        when(matches.save(match)).thenReturn(match);

        // act
        Match returnedMatch = matchService.updateMatch(match.getId(), match);

        //assert
        assertEquals(match.getResult(), returnedMatch.getResult());
        assertEquals(match.isBYE(), returnedMatch.isBYE());
        verify(matches, times(2)).save(match);
        verify(matches).findById(match.getId());
    }

    @Test
    void updateMatch_invalidId() {
        // arrange
        Match match = new Match();
        matchService.addMatch(match);
        match.setResult(1);
        match.setBYE(true);
        long invalidID = 0L;

        // mock the find by id operation
        when(matches.findById(invalidID)).thenReturn(Optional.empty());

        // act

        //assert
        assertThrows(MatchNotFoundException.class, () -> {
            matchService.updateMatch(invalidID, match);
        });
        verify(matches, times(1)).save(match);
        verify(matches).findById(invalidID);
    }
}