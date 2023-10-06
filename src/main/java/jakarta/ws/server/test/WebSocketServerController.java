package jakarta.ws.server.test;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.ws.server.test.convert.TextWebsocketVertoEventDecoder;
import lombok.RequiredArgsConstructor;
import jakarta.ws.server.test.chat.ChatRoom;
import jakarta.ws.server.test.chat.UserSession;
import jakarta.ws.server.test.convert.TextWebsocketVertoEventEncoder;
import jakarta.ws.server.test.event.IncomingWebsocketEvent;
import jakarta.ws.server.test.model.ClientMessage;
import jakarta.ws.server.test.model.LoginMessage;
import jakarta.ws.server.test.service.LookupService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ServerEndpoint(value = "/wstest",
        decoders = TextWebsocketVertoEventDecoder.class,
        encoders = TextWebsocketVertoEventEncoder.class)
@ApplicationScoped
@RequiredArgsConstructor
public class WebSocketServerController {

    private final LookupService lookupService;

    @OnOpen
    public void onOpen(Session session) {
//        log.info("onOpen> ");
    }

    @OnClose
    public void onClose(Session session) {
        log.info("onClose> ");

        final String roomName = (String) session.getUserProperties().get("room");
        final String userId = (String) session.getUserProperties().get("userId");

        ChatRoom chatRoom = lookupService.roomLookup(roomName);
        chatRoom.removeUser(userId);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("onError>: " + throwable);
    }

    @OnMessage
    public void onMessage(Session session, IncomingWebsocketEvent<?> event) {
        switch (event.getType()) {
            case LOGIN -> handleLogin(session, (LoginMessage) event.getEvent());
            case MESSAGE -> handleIncomingMessage(session, (ClientMessage) event.getEvent());
        }
    }

    private void handleIncomingMessage(Session session, ClientMessage clientMessage) {
        final String room = (String) session.getUserProperties().get("room");
        final ChatRoom chatRoom = lookupService.roomLookup(room);

        final UserSession userSession = chatRoom.getUserSessionById(clientMessage.getId());
        if (null != userSession) {
            userSession.handleIncomingMessage(clientMessage.getMsg());
        }
    }


    private void handleLogin(Session session, LoginMessage loginMessage) {
        final String roomName = loginMessage.getRoomId();
        final String userId = loginMessage.getUserId();

        session.getUserProperties().put("room", roomName);
        session.getUserProperties().put("userId", userId);

        ChatRoom chatRoom = lookupService.roomLookup(roomName);
        chatRoom.addUser(userId, session);
    }
}
