package wins.insomnia.mcitemhunt.model;

import lombok.Getter;

public class UserSession {

    /**
     * Maximum possible time in ms a session can remain valid.
     */
    public static final long forcedExpirationTimeout = 86_400_000; // 24 hr
    /**
     * How long a session can go in ms without receiving a communication from the client before expiring.
     */
    public static final long expirationTimeout = 3_600_000; // 1 hr

    @Getter
    private final String playerId;
    @Getter
    private final String sessionToken;
    @Getter
    private long latestCommunication;
    @Getter
    private long sessionCreationTime;

    public UserSession(String playerId, String sessionToken) {
        this.playerId = playerId;
        this.sessionToken = sessionToken;

        sessionCreationTime = System.currentTimeMillis();
        latestCommunication = sessionCreationTime;
    }

    /**
     *
     * Refreshes time-to-live / timeout.
     *
     */
    public void gotCommunication() {
        this.latestCommunication = System.currentTimeMillis();
    }


    /**
     *
     * Uses currentTime param instead of grabbing current time at method call.
     *
     * @param currentTime The current time in ms. (System.currentTimeMillis)
     * @return Boolean describing if this session is expired (true) or not (false).
     */
    public boolean isExpired(long currentTime) {
        if (currentTime - sessionCreationTime > forcedExpirationTimeout) {
            return true;
        }
        return currentTime - latestCommunication >  expirationTimeout;
    }

    /**
     * @return Boolean describing if this session is expired (true) or not (false).
     */
    public boolean isExpired() {
        return isExpired(System.currentTimeMillis());
    }

}


