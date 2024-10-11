package csd.grp3.Testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.tournament.TournamentServiceImpl;
// import csd.grp3.user.User;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private Tournament tournament;
    // private User player;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    //     tournament = new Tournament();
    //     tournament.setId(1L);
    //     tournament.setTitle("Test Tournament");
    //     tournament.setSize(2);
        
    //     player = new User("testUser", "testPassword123");  // Username and password
    //     player.setAuthorities("ROLE_PLAYER"); // Set specific authorities
    }

    @Test
    void listTournaments_NoTournaments_ReturnEmptyList() {
        List<Tournament> tournamentsList = new ArrayList<>();

        // mock the getAllTournaments()
        when(tournamentRepository.getAllTournaments()).thenReturn(tournamentsList);

        List<Tournament> result = tournamentService.listTournaments();

        assertEquals(0, result.size());
        verify(tournamentRepository).getAllTournaments();
    }

    @Test
    void listTournaments_HasTournaments_ReturnListOfTournaments() {
        // Arrange
        Tournament tournament = new Tournament();
        List<Tournament> tournamentsList = new ArrayList<>();
        tournamentsList.add(tournament);
        
        // mock findAll()
        when(tournamentRepository.findAll()).thenReturn(tournamentsList);
        // mock adding

        List<Tournament> result = tournamentService.listTournaments();

        assertEquals(1, result.size());
        assertEquals("Test Tournament", result.get(0).getTitle());
        verify(tournamentRepository, times(1)).findAll();
    }

    @Test
    void getTournament_NoTournament_ReturnNull() {
        // mock findById
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act
        Tournament tournament = tournamentService.getTournament(0L);

        // Assert
        assertNull(tournament);
        verify(tournamentRepository).findById(0L);
    }

    @Test
    void getTournament_TournamentFound_ReturnTournament() {
        // Mock findById to return the tournament
        when(tournamentRepository.findById(1L))
        .thenReturn(Optional.of(tournament));

        // Act
        Tournament foundTournament = tournamentService.getTournament(1L);

        // Assert
        assertNotNull(foundTournament);
        assertEquals(1L, foundTournament.getId());
        assertEquals("Test Tournament", foundTournament.getTitle());
        verify(tournamentRepository).findById(1L);
    }

    @Test
    void testAddTournament() {
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament result = tournamentService.addTournament(tournament);

        assertEquals(tournament, result);
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testUpdateTournament() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(tournament);

        Tournament newTournamentInfo = new Tournament();
        newTournamentInfo.setTitle("Updated Tournament");

        Tournament updatedTournament = tournamentService.updateTournament(1L, newTournamentInfo);

        assertNotNull(updatedTournament);
        assertEquals("Updated Tournament", updatedTournament.getTitle());
        verify(tournamentRepository, times(1)).findById(1L);
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    void testGetTournament() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        Tournament result = tournamentService.getTournament(1L);

        assertNotNull(result);
        assertEquals(tournament.getId(), result.getId());
        verify(tournamentRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTournament() {
        doNothing().when(tournamentRepository).deleteById(1L);

        assertDoesNotThrow(() -> tournamentService.deleteTournament(1L));
        verify(tournamentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRegisterPlayer() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.registerUser(player, 1L);

        assertEquals(1, tournament.getUsers().size());
        assertEquals(player, tournament.getUsers().get(0));
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testWithdrawPlayer() {
        tournament.getUsers().add(player);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.withdrawUser(player, 1L);

        assertFalse(tournament.getUsers().contains(player));
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testTournamentExists() {
        // when(tournamentRepository.existsById(1L)).thenReturn(true);

        // boolean exists = tournamentService.tournamentExists(1L);

        // assertTrue(exists);
        // verify(tournamentRepository, times(1)).existsById(1L);
    }
}