package jakarta.ws.server.test.chat;

import jakarta.websocket.Session;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRoom  {

    private final Map<String, UserSession> sessionMap;
    private final AtomicLong messageCounter;


    public ChatRoom(Map<String, UserSession> sessionMap, AtomicLong messageCounter) {
        this.sessionMap = sessionMap;
        this.messageCounter = messageCounter;
    }


    public void addUser(String userId, Session session) {
        sessionMap.put(userId, new UserSession(session, this, messageCounter));
    }

    public UserSession getUserSessionById(String userId) {
        return sessionMap.get(userId);
    }

    public void removeUser(String userId) {
        sessionMap.remove(userId);
    }

    public Collection<UserSession> getUserSessions() {
        return sessionMap.values();
    }

}
