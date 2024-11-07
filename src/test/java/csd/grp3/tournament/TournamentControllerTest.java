package csd.grp3.tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import csd.grp3.user.User;

public class TournamentControllerTest {

    @Mock
    private TournamentRepository tournamentRepo;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TournamentController tournamentController;

    private Tournament tournament;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tournament = new Tournament(); // Adjust constructor or use setters to initialize properties
        user = new User("testuser", "password123");
    }

    @Test
    public void testGetAllTournaments_Success() {
        when(tournamentService.listTournaments()).thenReturn(List.of(tournament));

        ResponseEntity<List<Tournament>> response = tournamentController.getAllTournaments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetTournamentById_Success() {
        when(tournamentService.getTournament(1L)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.getTournamentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetTournamentById_NotFound() {
        when(tournamentService.getTournament(1L)).thenThrow(new TournamentNotFoundException(1L));

        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.getTournamentById(1L);
        });

        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddTournament_Success() {
        when(tournamentService.addTournament(tournament)).thenReturn(tournament);

        ResponseEntity<HttpStatus> response = tournamentController.addTournament(tournament);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTournamentById_Success() {
        when(tournamentService.updateTournament(1L, tournament)).thenReturn(tournament);

        ResponseEntity<HttpStatus> response = tournamentController.updateTournamentById(1L, tournament);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTournamentById_NotFound() {
        when(tournamentService.updateTournament(1L, tournament)).thenThrow(new TournamentNotFoundException(1L));

        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.updateTournamentById(1L, tournament);
        });

        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTournamentById_Success() {
        doNothing().when(tournamentService).deleteTournament(1L);

        ResponseEntity<HttpStatus> response = tournamentController.deleteTournamentById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTournamentById_NotFound() {
        doThrow(new TournamentNotFoundException(1L)).when(tournamentService).deleteTournament(1L);

        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.deleteTournamentById(1L);
        });

        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    public void testWithdrawUser_Success() {
        doNothing().when(tournamentService).withdrawUser(user, 1L);

        ResponseEntity<HttpStatus> response = tournamentController.withdraw(user, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testWithdrawUser_TournamentNotFound() {
        doThrow(new TournamentNotFoundException(1L)).when(tournamentService).withdrawUser(user, 1L);

        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.withdraw(user, 1L);
        });

        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    public void testRegisterUser_Success() {
        doNothing().when(tournamentService).registerUser(user, 1L);

        ResponseEntity<HttpStatus> response = tournamentController.registerUser(user, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testRegisterUser_PlayerAlreadyRegistered() {
        doThrow(new PlayerAlreadyRegisteredException()).when(tournamentService).registerUser(user, 1L);

        PlayerAlreadyRegisteredException exception = assertThrows(PlayerAlreadyRegisteredException.class, () -> {
            tournamentController.registerUser(user, 1L);
        });

        assertEquals("Player has already registered for this tournament.", exception.getMessage());
    }
}