package csd.grp3.Testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentNotFoundException;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.tournament.TournamentServiceImpl;
import csd.grp3.usertournament.UserTournamentRepository;
import csd.grp3.usertournament.UserTournamentServiceImpl;
import csd.grp3.user.User;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserTournamentRepository userTournamentRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @InjectMocks
    private UserTournamentServiceImpl userTournamentService;

    private Tournament tournament;
    private User player;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = new Tournament();
        tournament.setId(1L);
        tournament.setTitle("Test Tournament");
        tournament.setSize(2);
        tournament.setUserTournaments(new ArrayList<>());
        
        player = new User("testUser", "testPassword123");  // Username and password
        player.setAuthorities("ROLE_PLAYER"); // Set specific authorities
    }

    @Test
    void listTournaments_NoTournaments_ReturnEmptyList() {
        List<Tournament> tournamentsList = new ArrayList<>();

        // mock the getAllTournaments()
        when(tournamentRepository.getAllTournaments()).thenReturn(tournamentsList);

        List<Tournament> result = tournamentService.listTournaments();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(tournamentRepository).getAllTournaments();
    }

    @Test
    void listTournaments_HasTournaments_ReturnListOfTournaments() {
        // Arrange
        List<Tournament> tournamentsList = new ArrayList<>();
        tournamentsList.add(tournament);
        
        // mock getAllTournaments()
        when(tournamentRepository.getAllTournaments()).thenReturn(tournamentsList);

        List<Tournament> result = tournamentService.listTournaments();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tournamentRepository).getAllTournaments();
        // assertEquals("Test Tournament", result.get(0).getTitle());
        // verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void getTournament_NoTournament_ReturnTournamentNotFoundException() {
        // Mock findById to return an empty Optional
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act & Assert: Expect TournamentNotFoundException to be thrown
        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentService.getTournament(1L);
        });

        // Verify that the exception message is correct
        assertEquals("Could not find tournament 1", exception.getMessage());

        // Verify that findById was called with the correct argument
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void getTournament_TournamentFound_ReturnTournament() {
        // Mock findById to return the tournament
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(tournament));

        // Act
        Tournament foundTournament = tournamentService.getTournament(1L);

        // Assert
        assertNotNull(foundTournament);
        assertEquals(1L, foundTournament.getId());
        assertEquals("Test Tournament", foundTournament.getTitle());
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void addTournament_NewTitle_ReturnSavedTournament() {
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament result = tournamentService.addTournament(tournament);

        assertEquals(tournament, result);
        verify(tournamentRepository).save(tournament);
    }

    @Test
    void addTournament_SameTitle_ReturnSavedTournamentWithDifferentID() {
        // Arrange
        Tournament tournament2 = tournament;
        tournament2.setId(2L);
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        // Act
        tournamentService.addTournament(tournament);
        Tournament result = tournamentService.addTournament(tournament2);

        // Assert
        assertEquals(2L, result.getId());
        assertEquals("Test Tournament", result.getTitle());
        verify(tournamentRepository, times(2)).save(tournament);
    }

    @Test
    void updateTournament_NotFound_ReturnTournamentNotFoundException() {
        // Arrange
        when(tournamentRepository.findById(1L)).thenReturn(Optional.empty());
        
        // Act
        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentService.updateTournament(1L, tournament);
        });

        // Assert
        assertEquals("Could not find tournament 1", exception.getMessage());
        verify(tournamentRepository).findById(1L);
    }

    // @Test
    // void updateTournament_UpdatedTournament_ReturnUpdatedTournament() {
    //     // Arrange
    //     Tournament newTournamentInfo = new Tournament(1L, null, "Updated Tournament", 0, 0, null, 0, new ArrayList<>());
    //     when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
    //     when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);
    //     when(userTournamentRepository.findRegisteredUsersByTournamentId(1L)).thenReturn(new ArrayList<>());
    //     when(userTournamentRepository.findWaitlistUsersByTournamentId(1L)).thenReturn(new ArrayList<>());

    //     // Act
    //     Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

    //     // Assert
    //     assertNotNull(updatedTournament);
    //     assertEquals("Updated Tournament", updatedTournament.getTitle());
    //     verify(tournamentRepository).findById(1L);
    //     verify(tournamentRepository).save(any(Tournament.class));
    // }

    @Test
    void deleteTournament_DeleteSuccess_ReturnDeletedTournament() {
        // mock getById method to return a Tournament
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.of(tournament));

        // mock the deleteById method to do nothing (since its a void method)
        doNothing().when(tournamentRepository).deleteById(tournament.getId());

        // act & assert
        assertDoesNotThrow(() -> tournamentService.deleteTournament(tournament.getId()));

        // verify delete called once with correct tournament ID
        verify(tournamentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTournament_TournamentNotFound_ReturnTournamentNotFoundException() {
        // mock deleteById method to do nothing (since its a void method)
        when(tournamentRepository.findById(tournament.getId())).thenReturn(Optional.empty());

        // Act & Assert: Expect TournamentNotFoundException to be thrown
        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentService.deleteTournament(tournament.getId());
        });

        // Verify that the exception message is correct
        assertEquals("Could not find tournament 1", exception.getMessage());

        // Verify that deleteById was never called with the correct argument
        verify(tournamentRepository, never()).deleteById(1L);
    }

    @Test
    void registerPlayer_TournamentNotFound_ReturnTournamentNotFoundException() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(player);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(userTournamentRepository.findById(1L)).thenReturn(userList);

        tournamentService.registerUser(player, 1L);

        assertEquals(1, tournament.getUsers().size());
        assertEquals(player, tournament.getUsers().get(0));
        verify(tournamentRepository, times(1)).save(tournament);
    }

//     @Test
//     void testGetTournament() {
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         Tournament result = tournamentService.getTournament(1L);

//         assertNotNull(result);
//         assertEquals(tournament.getId(), result.getId());
//         verify(tournamentRepository, times(1)).findById(1L);
//     }



//     @Test
//     void testWithdrawPlayer() {
//         tournament.getUsers().add(player);
//         when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

//         tournamentService.withdrawUser(player, 1L);

//         assertFalse(tournament.getUsers().contains(player));
//         verify(tournamentRepository, times(1)).save(tournament);
//     }

//     @Test
//     void testTournamentExists() {
//         // when(tournamentRepository.existsById(1L)).thenReturn(true);

//         // boolean exists = tournamentService.tournamentExists(1L);

//         // assertTrue(exists);
//         // verify(tournamentRepository, times(1)).existsById(1L);
//     }
}