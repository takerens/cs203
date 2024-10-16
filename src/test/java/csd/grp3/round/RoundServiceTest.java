package csd.grp3.round;

import csd.grp3.tournament.Tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTest {

    @Mock
    private RoundRepository rounds;

    @InjectMocks
    private RoundServiceImpl roundService;

    @Test
    void createRound() {
        // arrange
        Tournament mockTournament = new Tournament();
        Round round = new Round(mockTournament);

        // mock the save operation
        when(rounds.save(round)).thenReturn(round);

        // act
        Round returnedRound = roundService.createRound(mockTournament);

        // assert
        assertEquals(round, returnedRound);
        verify(rounds).save(round);
    }

    @Test
    void getRound_validId() {
        // arrange
        Round round = new Round();

        // mock the save operation
        when(rounds.findById(round.getId())).thenReturn(Optional.of(round));

        // act
        Round returnedRound = roundService.getRound(round.getId());

        // assert
        assertEquals(round, returnedRound);
        verify(rounds).findById(round.getId());
    }

    @Test
    void getRound_invalidId() {
        // arrange
        Round invalid_round = new Round();

        // mock the save operation
        when(rounds.findById(invalid_round.getId())).thenReturn(Optional.empty());

        // act
        
        //assert
        assertThrows(RoundNotFoundException.class, () -> {
            roundService.getRound(invalid_round.getId());
        });
        verify(rounds).findById(invalid_round.getId());
    }
}
