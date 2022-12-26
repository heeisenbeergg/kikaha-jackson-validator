package kikaha.urouting.validations;

import kikaha.config.Config;
import kikaha.core.cdi.CDI;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class HttpRequestValidatorProducer {

	@Inject
	private CDI cdi;

	@Inject
	private Config kikahaConf;

	private HttpRequestValidator httpRequestValidator;

	@PostConstruct
	public void init() {
		final Class<?> clazz = kikahaConf.getClass("server.urouting.http-request-validator");

		if(clazz != null) {
			log.debug("Configured HTTP request validator: " + clazz);
			this.httpRequestValidator = (HttpRequestValidator) cdi.load(clazz);
		} else {
			log.debug("HTTP request validator not configured");
		}
	}

	@Produces
	public HttpRequestValidator produceDataSource() {
		return this.httpRequestValidator;
	}

}
