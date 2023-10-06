package jakarta.ws.server.test.convert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Decoder;
import jakarta.ws.server.test.event.type.IncomingEventType;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import jakarta.ws.server.test.event.IncomingWebsocketEvent;
import jakarta.ws.server.test.model.ClientMessage;
import jakarta.ws.server.test.model.LoginMessage;
import jakarta.ws.server.test.util.CustomJacksonObjectMapper;


@Log4j2
public class TextWebsocketVertoEventDecoder implements Decoder.Text<IncomingWebsocketEvent<?>> {

    private static final ObjectMapper jackson = CustomJacksonObjectMapper.getJackson();

    @Override
    @SneakyThrows
    public IncomingWebsocketEvent<?> decode(String s) {
//        log.info("RESPONSE: {}", s);
        final String eventType = jackson.readTree(s).get("type").asText();
        final JsonNode payload = jackson.readTree(s).get("source");
        return switch (IncomingEventType.valueOf(eventType)) {
            case LOGIN -> IncomingWebsocketEvent.login(jackson.treeToValue(payload, LoginMessage.class));
            case MESSAGE -> IncomingWebsocketEvent.message(jackson.treeToValue(payload, ClientMessage.class));
        };
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

}