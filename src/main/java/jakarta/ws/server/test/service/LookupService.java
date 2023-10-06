package jakarta.ws.server.test.service;

import jakarta.ws.server.test.chat.ChatRoom;


public interface LookupService {

    ChatRoom roomLookup(String roomContextKey);

}
