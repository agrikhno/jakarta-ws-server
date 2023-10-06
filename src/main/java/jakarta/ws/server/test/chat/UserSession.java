package jakarta.ws.server.test.chat;

import jakarta.websocket.Session;
import jakarta.ws.server.test.event.OutgoingWebsocketEvent;
import jakarta.ws.server.test.model.ChatMessage;
import jakarta.ws.server.test.model.OutgoingChatMessage;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class UserSession {

    private final Session session;
    private final ChatRoom chatRoom;
    private final AtomicLong messageCounter;

    public void handleIncomingMessage(String message) {
        OutgoingWebsocketEvent<OutgoingChatMessage> outgoingWebsocketEvent = OutgoingWebsocketEvent
                .message(OutgoingChatMessage
                        .builder()
                        .message(
                                ChatMessage.builder()
                                        .id(messageCounter.incrementAndGet())
                                        .userName("UserName")
                                        .message(message)
                                        .build()
                        )
                        .build()
                );

        for (UserSession userSession : chatRoom.getUserSessions()) {
            userSession.handleOutgoingMessage(outgoingWebsocketEvent);
        }
    }

    public void handleOutgoingMessage(OutgoingWebsocketEvent<OutgoingChatMessage> outgoingWebsocketEvent) {
        session.getAsyncRemote().sendObject(outgoingWebsocketEvent);
    }

}
