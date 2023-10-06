package jakarta.ws.server.test.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.ws.server.test.chat.ChatRoom;
import jakarta.ws.server.test.service.LookupService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class LookupServiceProducer {


    @Produces
    @Singleton
    public static LookupService lookupService() {
        final Map<String, ChatRoom> refKeyChatRoomMap = new HashMap<>();
        final AtomicLong messageCounter = new AtomicLong();

        final int roomCount = 10;

        for (int i = 1; i <= roomCount; i++) {
            refKeyChatRoomMap.put("room_" + i, new ChatRoom(new ConcurrentHashMap<>(), messageCounter));
        }

        return new DefaultLookupService(refKeyChatRoomMap);
    }
}
