package kikaha.urouting.serializers.jackson;

import java.io.IOException;
import javax.inject.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import kikaha.core.modules.http.ContentType;
import kikaha.core.modules.websocket.WebSocketSession;
import kikaha.urouting.api.Mimes;
import kikaha.urouting.validations.HttpRequestValidator;

import static java.util.Objects.nonNull;

/**
 * WebSocket JSON serializer and unserializer.
 */
@Singleton
@ContentType(Mimes.JSON)
public class JSONWebSocketSerializer implements WebSocketSession.Serializer, WebSocketSession.Unserializer {

	@Inject
	private Jackson jackson;

	@Inject
	private HttpRequestValidator httpRequestValidator;

	@Override
	public String serialize(Object object) throws JsonProcessingException {
		return jackson.objectMapper().writeValueAsString( object );
	}

	@Override
	public <T> T unserialize(String bodyData, Class<T> expectedClass) throws IOException {
		final T data = jackson.objectMapper().readValue(bodyData, expectedClass);
		final boolean hasHttpRequestValidator = nonNull(this.httpRequestValidator);

		if(hasHttpRequestValidator) {
			this.httpRequestValidator.validate(data);
		}

		return data;
	}
}
