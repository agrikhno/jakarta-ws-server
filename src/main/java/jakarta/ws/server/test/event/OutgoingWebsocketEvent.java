package jakarta.ws.server.test.event;

import jakarta.ws.server.test.event.type.OutgoingEventType;
import jakarta.ws.server.test.model.OutgoingChatMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutgoingWebsocketEvent<T> {

    /**
     * Event type
     */
    private OutgoingEventType type;

    /**
     * Event payload
     */
    private T source;

    public static OutgoingWebsocketEvent<OutgoingChatMessage> message(OutgoingChatMessage outgoingChatMessage) {
        return OutgoingWebsocketEvent.<OutgoingChatMessage>builder()
                .type(OutgoingEventType.MESSAGE)
                .source(outgoingChatMessage)
                .build();
    }
}
