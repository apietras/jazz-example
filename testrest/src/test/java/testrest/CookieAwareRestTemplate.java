package testrest;

import java.net.URI;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class CookieAwareRestTemplate extends RestTemplate {
    private final HttpClient httpClient;
    private final CookieStore cookieStore;
    
    public CookieStore getCookieStore() {
		return cookieStore;
	}

	private final HttpContext httpContext;
    private final StatefullHttpRequestFactory statefullHttpRequestFactory;

    public CookieAwareRestTemplate()
    {
        super();
        cookieStore = new BasicCookieStore();
        httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        httpContext = new BasicHttpContext();
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        statefullHttpRequestFactory = new StatefullHttpRequestFactory(httpClient, httpContext);
        super.setRequestFactory(statefullHttpRequestFactory);
    }

}

class StatefullHttpRequestFactory extends HttpComponentsClientHttpRequestFactory
{
    private final HttpContext httpContext;

    public StatefullHttpRequestFactory(HttpClient httpClient, HttpContext httpContext)
    {
        super(httpClient);
        this.httpContext = httpContext;
    }

    @Override
    protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
    {
        return this.httpContext;
    }
}
