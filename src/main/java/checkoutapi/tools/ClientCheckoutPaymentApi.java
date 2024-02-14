package checkoutapi.tools;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientCheckoutPaymentApi {
	@Value("${app.ssl.checkout-payment-api.keyStorePass}")
	private char[] allPassword;

	@Value("${app.ssl.checkout-payment-api.keyStore}")
	private String keyStore;

	@Bean
	public RestTemplate restTemplateCheckoutPaymentApi() throws Exception {
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
		return restTemplate;
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
		SSLContext sslContext = SSLContextBuilder.create()
				.loadKeyMaterial(ResourceUtils.getFile(keyStore), allPassword, allPassword)
				.loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

		// FIXME Remove when you can add the appropriate domain in your hosts file
		// hosts file -> c:/Window/System32/etc/diver/hosts or /etc/hosts
		// -> 127.0.0.1 local.localhost
		// IMPORTANT: To disable Host Name validation, when run as localhost.
		// allowAllHostnameVerifier() or
		// .setHostnameVerifier(AllowAllHostnameVerifier.INSTANCE)
		// or new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext,
				NoopHostnameVerifier.INSTANCE);
		HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
				.setSSLSocketFactory(sslSocketFactory).build();

		HttpClient httpClient = HttpClients.custom().setConnectionManager(cm).evictExpiredConnections().build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient);
		return clientHttpRequestFactory;
	}
}
