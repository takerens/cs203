package csd.grp3.match;

import csd.grp3.round.Round;
import csd.grp3.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

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
        match.setResult(1.0);
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
        match.setResult(1.0);
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

    @Test
    void deleteMatch() {
        // arrange
        Match match = new Match();
        matchService.addMatch(match);

        // act
        matchService.deleteMatch(match.getId());

        //assert
        assertThrows(MatchNotFoundException.class, () -> {
            matchService.getMatch(match.getId());
        });
        verify(matches).deleteById(match.getId());
    }

    @Test
    void getRoundMatches_validRound() {
        // arrange
        Round round = new Round();
        Match match1 = new Match();
        match1.setRound(round);
        matchService.addMatch(match1);
        Match match2 = new Match();
        match2.setRound(round);
        matchService.addMatch(match2);

        List<Match> expectedMatches = Arrays.asList(match1, match2);
        //mock the findByRound
        when(matches.findByRound(round)).thenReturn(expectedMatches);

        // act
        List<Match> returnedMatches = matchService.getRoundMatches(round);

        //assert
        assertEquals(2, returnedMatches.size());
        assertTrue(returnedMatches.contains(match1));
        assertTrue(returnedMatches.contains(match2));
        verify(matches).findByRound(round);
    }

    @Test
    void getRoundMatches_invalidRound() {
        // arrange
        Round invalidRound = new Round();

        //mock the findByRound
        when(matches.findByRound(invalidRound)).thenReturn(Collections.emptyList());

        // act
        List<Match> returnedMatches = matchService.getRoundMatches(invalidRound);

        //assert
        assertTrue(returnedMatches.isEmpty());
        verify(matches).findByRound(invalidRound);
    }

    @Test
    void getUserMatches_validUser() {
        // arrange
        User user = new User();
        Match match1 = new Match();
        match1.setWhite(user);
        matchService.addMatch(match1);
        Match match2 = new Match();
        match2.setBlack(user);
        matchService.addMatch(match2);
        Match match3 = new Match();
        match3.setWhite(user);
        matchService.addMatch(match3);

        List<Match> expectedMatches = Arrays.asList(match1, match2, match3);
        //mock the findByBlackOrWhite
        when(matches.findByBlackOrWhite(user, user)).thenReturn(expectedMatches);

        // act
        List<Match> returnedMatches = matchService.getUserMatches(user);

        //assert
        assertEquals(3, returnedMatches.size());
        assertTrue(returnedMatches.contains(match1));
        assertTrue(returnedMatches.contains(match2));
        assertTrue(returnedMatches.contains(match3));
        verify(matches).findByBlackOrWhite(user, user);
    }

    @Test
    void getUserMatches_invalidUser() {
        // arrange
        User invalidUser = new User("User", "password");

        //mock the findByBlackOrWhite
        when(matches.findByBlackOrWhite(invalidUser, invalidUser)).thenReturn(Collections.emptyList());

        // act
        List<Match> returnedMatches = matchService.getUserMatches(invalidUser);

        //assert
        assertTrue(returnedMatches.isEmpty());
        verify(matches).findByBlackOrWhite(invalidUser, invalidUser);
    }

    @Test
    void getMatchesBetweenTwoUsers_withMatch() {
        // arrange
        User user1 = new User("User1", "password1");
        User user2 = new User("User2", "password2");
        Match match1 = new Match();
        match1.setWhite(user1);
        match1.setBlack(user2);
        matchService.addMatch(match1);
        Match match2 = new Match();
        match2.setWhite(user2);
        match2.setBlack(user1);
        matchService.addMatch(match2);

        List<Match> expectedMatches = Arrays.asList(match1, match2);
        //mock the findByBlackAndWhiteOrWhiteAndBlack
        when(matches.findByBlackAndWhiteOrWhiteAndBlack(user1, user2, user2, user1)).thenReturn(expectedMatches);

        // act
        List<Match> returnedMatches = matchService.getMatchesBetweenTwoUsers(user1, user2);

        //assert
        assertEquals(2, returnedMatches.size());
        assertTrue(returnedMatches.contains(match1));
        assertTrue(returnedMatches.contains(match2));
        verify(matches).findByBlackAndWhiteOrWhiteAndBlack(user1, user2, user2, user1);
    }

    @Test
    void getMatchesBetweenTwoUsers_noMatch() {
        // arrange
        User user1 = new User("User1", "password1");
        User user2 = new User("User2", "password2");

        //mock the findByBlackAndWhiteOrWhiteAndBlack
        when(matches.findByBlackAndWhiteOrWhiteAndBlack(user1, user2, user2, user1)).thenReturn(Collections.emptyList());

        // act
        List<Match> returnedMatches = matchService.getMatchesBetweenTwoUsers(user1, user2);

        //assert
        assertTrue(returnedMatches.isEmpty());
        verify(matches).findByBlackAndWhiteOrWhiteAndBlack(user1, user2, user2, user1);
    }
}