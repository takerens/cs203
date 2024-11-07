package csd.grp3.round;

import org.springframework.stereotype.Service;

import csd.grp3.tournament.Tournament;

@Service
public interface RoundService {
    Round getRound(Long id);
    Round createRound(Tournament tournament);
}
