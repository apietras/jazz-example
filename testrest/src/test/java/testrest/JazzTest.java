package testrest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(BlockJUnit4ClassRunner.class)
public class JazzTest {
    
private static final String RQM_LOGIN = "apietras";
private static final String RQM_PASSWORD = "jakjanienawidzeibm";
private static final String RQM_URL = "https://jazz.net/sandbox02-qm/";
private static final String PROJECT_NAME = "apietras's Project (Quality Management)";
private static final String RQM_TESTCASE_ID = "4995";
CookieAwareRestTemplate restTemplate;
    
    public JazzTest() {
        restTemplate = new CookieAwareRestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        converters.addAll(restTemplate.getMessageConverters());
        restTemplate.setMessageConverters(converters);
    }
    
    @Test
    public void testyLogowania() {
        /* Logowanie */
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("j_username", RQM_LOGIN);
        map.add("j_password", RQM_PASSWORD);
        ResponseEntity<String> response = restTemplate.postForEntity(RQM_URL + "j_security_check", map , String.class);
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Headers");
        for (Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
            System.out.println("\t("+entry.getKey()+"):="+toString(entry.getValue()));
        }
        if (response.getBody()!=null) {
            System.out.println("Response body size: " + response.getBody().length());
        }
        /* rysurs */
        System.out.println("===========");
        
        String resourceUrl = RQM_URL + "service/com.ibm.rqm.integration.service.IIntegrationService/resources/"+PROJECT_NAME+"/testcase/urn:com.ibm.rqm:testcase:"+RQM_TESTCASE_ID;
        ResponseEntity<TestCaseXmlModel> response2 = restTemplate.getForEntity(resourceUrl , TestCaseXmlModel.class);
        System.out.println("Status code: " + response2.getStatusCode());
        System.out.println("Headers");
        for (Entry<String, List<String>> entry : response2.getHeaders().entrySet()) {
            System.out.println("\t("+entry.getKey()+"):="+toString(entry.getValue()));
        }
        if (response2.getBody()!=null) {
            System.out.println("Got test case: " + response2.getBody().toString());
        }
    }
    
    private String toString(List<String> value) {
        StringBuilder builder = new StringBuilder("[");
        for (String part : value) {
            if (builder.length()>2) {
                builder.append(", ");
            }
            builder.append(part);
        }
        builder.append(']');
        return builder.toString();
    }
    
}
