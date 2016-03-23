package testrest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JazzClient {
    private String rqmLogin = "apietras";
    private String rqmPassword = "jakjanienawidzeibm";
    private String rqmUrl = "https://jazz.net/sandbox02-qm";
    private String rqmProjectName = "apietras's Project (Quality Management)";
    private CookieAwareRestTemplate restTemplate;

    public JazzClient(String rqmLogin, String rqmPassword, String rqmUrl, String rqmProjectName,
            CookieAwareRestTemplate restTemplate) {
        this();
        this.rqmLogin = rqmLogin;
        this.rqmPassword = rqmPassword;
        this.rqmUrl = rqmUrl;
        this.rqmProjectName = rqmProjectName;
        this.restTemplate = restTemplate;
    }
    
    public JazzClient() {
        restTemplate = new CookieAwareRestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        converters.addAll(restTemplate.getMessageConverters());
        restTemplate.setMessageConverters(converters);
    }
    
    public boolean authenticate() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("j_username", rqmLogin);
        map.add("j_password", rqmPassword);
        ResponseEntity<String> response = restTemplate.postForEntity(rqmUrl + "/j_security_check", map , String.class);
        if (response.getStatusCode().equals(HttpStatus.FOUND)) {
            //assumption that authentication failure results in other status code - undocumented
            return true;
        }
        return false;
    }
    
    public TestCaseXmlModel getTestCase(Integer testCaseId) {
        try {
            String resourceUrl = rqmUrl + "/service/com.ibm.rqm.integration.service.IIntegrationService/resources/"+rqmProjectName+"/testcase/urn:com.ibm.rqm:testcase:"+testCaseId;
            ResponseEntity<TestCaseXmlModel> response2 = restTemplate.getForEntity(resourceUrl , TestCaseXmlModel.class);
            if (response2.getStatusCode().equals(HttpStatus.OK)) {
                return response2.getBody();
            } else {
                //log status code
            }
        } catch (Exception e) {
            //log exception properly
        }
        return null;
    }
}
