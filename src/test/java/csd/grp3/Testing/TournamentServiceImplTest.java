package csd.grp3.Testing;

import csd.grp3.user.User;
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

import csd.grp3.player.Player;
import csd.grp3.tournament.Tournament;
import csd.grp3.tournament.TournamentRepository;
import csd.grp3.tournament.TournamentServiceImpl;;

public class TournamentServiceImplTest {

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @Mock
    private TournamentRepository tournamentRepository;

    private Tournament tournament;
    private User player;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = new Tournament();
        tournament.setId(1L);
        tournament.setTitle("Test Tournament");
        tournament.setSize(2);
        tournament.setPlayers(new ArrayList<>());
        tournament.setWaitingList(new ArrayList<>());
        
        player = new User("testUser", "testPassword123");  // Username and password
        player.setAuthorities("ROLE_PLAYER"); // Set specific authorities
    }

    @Test
    void testListTournaments() {
        List<Tournament> tournamentsList = new ArrayList<>();
        tournamentsList.add(tournament);
        
        when(tournamentRepository.findAll()).thenReturn(tournamentsList);

        List<Tournament> result = tournamentService.listTournaments();

        assertEquals(1, result.size());
        assertEquals("Test Tournament", result.get(0).getTitle());
        verify(tournamentRepository, times(1)).findAll();
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

        tournamentService.registerPlayer(player, 1L);

        assertEquals(1, tournament.getPlayers().size());
        assertEquals(player, tournament.getPlayers().get(0));
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testWithdrawPlayer() {
        tournament.getPlayers().add(player);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.withdrawPlayer(player, 1L);

        assertFalse(tournament.getPlayers().contains(player));
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    void testTournamentExists() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);

        boolean exists = tournamentService.tournamentExists(1L);

        assertTrue(exists);
        verify(tournamentRepository, times(1)).existsById(1L);
    }
}