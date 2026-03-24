package wins.insomnia.mcitemhunt.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wins.insomnia.mcitemhunt.model.UserSession;
import wins.insomnia.mcitemhunt.model.dto.ItemHuntRunDTO;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ActiveRunsManager {

    /**
     * Stores users attempting to grab a session id/login who have not been validated by Mojang servers.
     * Keys are server-id hashes and values are player ids.
     */
    private final ConcurrentHashMap<String, PendingUser> pendingUsers = new ConcurrentHashMap<>();

    /**
     * Map of all players who have an active session. If a user is not in this map, they are not allowed
     * to communicate with the api at all.
     */
    private final ConcurrentHashMap<String, UserSession> activeSessions = new ConcurrentHashMap<>();

    /**
     *
     * {@link ConcurrentHashMap} holding all active runs before they are finalized and entered into the db.
     * Keys are player ids (uuid), and values are data objects of active runs.
     *
     */
    private final ConcurrentHashMap<String, ItemHuntRunDTO> activeRuns = new ConcurrentHashMap<>();

    /**
     * Removes active runs if they have not received any new events within 5 minutes of the previous event.
     * Called every 1 minute.
     */
    @Scheduled(fixedRate = 60_000)
    public void cleanStaleActiveRuns() {
        long currentTime = System.currentTimeMillis();

        activeRuns.entrySet().removeIf(entry -> (
                currentTime - entry.getValue().getStartTime() > 300_000)
        );
    }

    /**
     * Removes any expired sessions.
     */
    @Scheduled(fixedRate = 600_000)
    public void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        activeSessions.entrySet().removeIf(
                entry -> (entry.getValue().isExpired(currentTime))
        );
    }



    public void addRun(String playerId, ItemHuntRunDTO runData) {
        activeRuns.put(playerId, runData);
    }

    public void removeRun(String playerId) {
        activeRuns.remove(playerId);
    }

    public ItemHuntRunDTO getRun(String playerId) {
        return activeRuns.get(playerId);
    }


    public void setPendingUser(String serverId, String playerId) {
        pendingUsers.put(serverId, new PendingUser(playerId, serverId, System.currentTimeMillis()));
    }

    public boolean isPendingUser(String serverId, String playerId) {
        PendingUser pendingUser = pendingUsers.get(serverId);
        if (pendingUser == null) return false;

        return pendingUser.playerId().equals(playerId);
    }

    /**
     * Cleans pending users from memory which have timed out of authentication.
     */
    @Scheduled(fixedRate = 60_000)
    public void cleanPendingUsers() {

        long currentTime = System.currentTimeMillis();
        int expirationDuration = 30_000;

        pendingUsers.keySet().removeIf(serverId -> (
                currentTime - pendingUsers.get(serverId).startTime() > expirationDuration
        ));
    }

    public void addSession(UserSession session) {
        activeSessions.put(session.getPlayerId(), session);
    }

    /**
     * Returns false if the player has no session or if the session is expired. Returns true otherwise.
     * @param playerId The uuid of the player you would like to check.
     * @return Boolean decribing if the player with uuid of playerId has an active session that has not expired.
     */
    public boolean hasValidSession(String playerId) {
        if (!activeSessions.containsKey(playerId)) return false;
        return !activeSessions.get(playerId).isExpired();
    }

    /**
     * Validates the player with uuid of playerId with a sessionToken against the active sessions map.
     * If the player does not have a valid session or the session is expired, returns false. Returns true otherwise.
     * @param playerId
     * @param sessionToken
     * @return
     */
    public boolean isValidSession(String playerId, String sessionToken) {
        if (!hasValidSession(playerId)) return false;
        return activeSessions.get(playerId).getSessionToken().equals(sessionToken);
    }


    /**
     * Record detailing a user trying to login/authenticate.
     * @param playerId
     * @param serverId
     * @param startTime
     */
    public record PendingUser(String playerId, String serverId, long startTime) {};
}
