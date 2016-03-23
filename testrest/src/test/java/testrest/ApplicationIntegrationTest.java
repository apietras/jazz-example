package testrest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest("server.port: 8080")
public class ApplicationIntegrationTest {

	CookieAwareRestTemplate restTemplate;
	
	public ApplicationIntegrationTest() {
		restTemplate = new CookieAwareRestTemplate();
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(new FormHttpMessageConverter());
		converters.addAll(restTemplate.getMessageConverters());
		restTemplate.setMessageConverters(converters);
	}

	@Test
	public void executeThreeNormalRequests() {
		System.out.println("=====================================Normal-Normal-Normal=====================================");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executeRequest();
		executeRequest();
		executeRequest();
	}
	
	@Test
	public void executeNormalExchangeNormalRequests() throws URISyntaxException {
		System.out.println("=====================================Normal-Exchange-Normal=====================================");
		executeRequest();
		executeExchangeRequest();
		executeRequest();
	}
	
	private void executeExchangeRequest() throws URISyntaxException {
		System.out.println("=====================================");
		System.out.println(":CookiesBefore - cookiestore");
		printCookies();
		System.out.println(":Response");
		RequestEntity<Void> requestEntity = RequestEntity.get(new URI("http://localhost:8080/")).header("X-NothingInteresting", "application/nothing").build();
		ResponseEntity<String> resp = restTemplate.exchange(requestEntity, String.class);
		System.out.println(resp.getBody());
		System.out.println(":CookiesAfter - cookiestore");
		printCookies();
		System.out.println();
	}

	public void executeRequest() {
		System.out.println("=====================================");
		System.out.println(":CookiesBefore - cookiestore");
		printCookies();
		System.out.println(":Response");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("j_username", "user");
		map.add("j_password", "password");
		String response = restTemplate.postForObject("http://localhost:8080/", map , String.class);
		System.out.println(response);
		System.out.println(":CookiesAfter - cookiestore");
		printCookies();
		System.out.println();
		
	}

	private void printCookies() {
		CookieStore cs = restTemplate.getCookieStore();
		for (Cookie c : cs.getCookies()) {
			System.out.println("\t["+c.getName()+"]:="+c.getValue());
		}
	}
	
}
