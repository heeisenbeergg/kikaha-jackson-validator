package kikaha.urouting.validations;

import java.io.IOException;

public interface HttpRequestValidator {

	public <T> void validate(final T data) throws IOException;

}