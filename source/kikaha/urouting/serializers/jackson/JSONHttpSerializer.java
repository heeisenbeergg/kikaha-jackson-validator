package kikaha.urouting.serializers.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import kikaha.core.modules.http.ContentType;
import kikaha.urouting.api.Mimes;
import kikaha.urouting.api.Serializer;
import kikaha.urouting.api.Unserializer;
import kikaha.urouting.validations.HttpRequestValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.ByteBuffer;

import static java.util.Objects.nonNull;

@Singleton
@ContentType(Mimes.JSON)
public class JSONHttpSerializer implements Serializer, Unserializer {

	@Inject
	private Jackson jackson;

	@Inject
	private HttpRequestValidator httpRequestValidator;

	@Override
	public <T> void serialize(T object, HttpServerExchange exchange, String encoding) throws IOException {
		final byte[] bytes = jackson.objectMapper().writeValueAsBytes(object);

		this.send(exchange, ByteBuffer.wrap(bytes));
	}

	public void send(final HttpServerExchange exchange, final ByteBuffer buffer) {
		exchange.getResponseSender().send( buffer );
	}

	@Override
	public <T> T unserialize(final HttpServerExchange exchange, final Class<T> targetClass, byte[] bodyData, final String encoding ) throws IOException {
		final T data = jackson.objectMapper().readValue(bodyData, targetClass);
		final boolean hasHttpRequestValidator = nonNull(this.httpRequestValidator);

		if(hasHttpRequestValidator) {
			this.httpRequestValidator.validate(data);
		}

		return data;
	}

}
