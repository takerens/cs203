package csd.grp3.profile;

import org.springframework.stereotype.Service;

import csd.grp3.user.User;

@Service
public interface ProfileService {
    public Profile getProfileByUser(User user);
    public void modifyElo(User user, int newElo);
    public void modifyDisplayName(User user, String displayName);
    public void addHistory(User user, Long id);
    public void addRegistered(User user, Long id);
    public void removeRegistered(User user, Long id);
}
