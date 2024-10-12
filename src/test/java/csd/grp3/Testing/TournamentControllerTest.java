package csd.grp3.Testing;

import csd.grp3.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import csd.grp3.tournament.*;

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
        tournament = new Tournament(); // Adjusted to use default constructor or create a constructor that matches the parameters
        user = new User("testuser", "password123");
    }

    @Test
    public void testGetAllTournaments_Success() {
        when(tournamentService.listTournaments()).thenReturn(List.of(tournament));

        ResponseEntity<List<Tournament>> response = tournamentController.getAllTournaments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertNotNull(response.getBody().get(0));
        assertEquals(tournament.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    public void testGetTournamentById_Success() {
        when(tournamentService.getTournament(1L)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.getTournamentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tournament.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void testGetTournamentById_NotFound() {
        // Mock the behavior of the tournamentService to throw the exception when the ID is not found
        when(tournamentService.getTournament(1L)).thenThrow(new TournamentNotFoundException(1L));

        // Call the controller method and capture the response
        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.getTournamentById(1L);
        });

        // Optionally, verify the exception message if needed
        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddTournament_Success() {
        when(tournamentRepo.save(tournament)).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.addTournament(tournament);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tournament.getTitle(), response.getBody().getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAddTournament_BadRequest() {
        // Simulate invalid input by setting the tournament title to null
        tournament.setTitle(null);

        // Mock the behavior of the tournamentRepo to throw an exception due to bad request
        when(tournamentRepo.save(any(Tournament.class))).thenThrow(new RuntimeException("Bad Request"));

        // Verify that the exception is thrown when calling the controller method
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tournamentController.addTournament(tournament);
        });

        // Optionally, verify the exception message if needed
        assertEquals("Bad Request", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTournamentById_Success() {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentRepo.save(any(Tournament.class))).thenReturn(tournament);

        ResponseEntity<Tournament> response = tournamentController.updateTournamentById(1L, tournament);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tournament.getTitle(), response.getBody().getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTournamentById_NotFound() {
        // Mock the behavior of the tournamentRepo to return empty (tournament not found)
        when(tournamentRepo.findById(1L)).thenReturn(Optional.empty());

        // Verify that the exception is thrown when calling the controller method
        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.updateTournamentById(1L, tournament);
        });

        // Optionally, verify the exception message if needed
        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTournamentById_Success() {
        doNothing().when(tournamentRepo).deleteById(1L);

        ResponseEntity<HttpStatus> response = tournamentController.deleteTournamentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTournamentById_NotFound() {
        // Mock the service to throw TournamentNotFoundException when deleteTournament is called
        doThrow(new TournamentNotFoundException(1L)).when(tournamentService).deleteTournament(1L);

        // Call the controller method and capture the response
        ResponseEntity<HttpStatus> response = tournamentController.deleteTournamentById(1L);

        // Assert that the response status is NOT_FOUND (404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testWithdrawUser_Success() {
        doNothing().when(tournamentService).withdrawUser(user, 1L);

        ResponseEntity<Void> response = tournamentController.withdraw(user, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testWithdrawUser_TournamentNotFound() {
        // Mock the behavior of the tournamentService to throw the exception when the tournament is not found
        doThrow(new TournamentNotFoundException(1L)).when(tournamentService).withdrawUser(user, 1L);

        // Verify that the exception is thrown when calling the controller method
        TournamentNotFoundException exception = assertThrows(TournamentNotFoundException.class, () -> {
            tournamentController.withdraw(user, 1L);
        });

        // Optionally, verify the exception message if needed
        assertEquals("Could not find tournament 1", exception.getMessage());
    }

    @Test
    public void testRegisterUser_Success() {
        doNothing().when(tournamentService).registerUser(user, 1L);

        ResponseEntity<Void> response = tournamentController.registerUser(user, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegisterUser_TournamentFullOrAlreadyRegistered() {
        // Mock the behavior of the tournamentService to throw an exception if user is already registered
        doThrow(new PlayerAlreadyRegisteredException()).when(tournamentService).registerUser(user, 1L);

        // Verify that the exception is thrown when calling the controller method
        PlayerAlreadyRegisteredException exception = assertThrows(PlayerAlreadyRegisteredException.class, () -> {
            tournamentController.registerUser(user, 1L);
        });

        // Optionally, verify the exception message if needed
        assertEquals("Player has already registered for this tournament.", exception.getMessage());
    }
}
