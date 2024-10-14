package csd.grp3.round;

import csd.grp3.tournament.Tournament;

public interface RoundService {
    Round getRound(Long id);
    Round createRound(Tournament tournament);
}
