package testrest;

import java.time.LocalDateTime;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
    	Cookie cookie = new Cookie("timestamp", LocalDateTime.now().toString());
		response.addCookie(cookie);
		StringBuilder responseString = new StringBuilder();
		if (request.getCookies() != null) {
			responseString.append("Request cookies found:\n");
			for (Cookie excookie: request.getCookies()) {
				responseString.append(String.format("\t[%s] := %s\n", excookie.getName(), excookie.getValue()));
			}
		} else {
			responseString.append("No cookies found in request\n");
		}
		if (request.getHeaderNames() != null) {
			responseString.append("Headers found:\n");
			Enumeration<String> headernames = request.getHeaderNames();
			while (headernames.hasMoreElements()) {
				String head = headernames.nextElement();
				responseString.append(String.format("\t[%s] := %s\n", head, enumerationToString(request.getHeaders(head))));
			}
		} else {
			responseString.append("No headers found\n");
		}
        return responseString.toString();
    }
    
    private String enumerationToString(Enumeration<String> enumeration) {
    	StringBuilder builder = new StringBuilder("[");
    	while (enumeration.hasMoreElements()) {
    		builder.append(enumeration.nextElement());
    		if (enumeration.hasMoreElements()) {
    			builder.append(", ");
    		}
    	}
    	builder.append(']');
    	return builder.toString();
    }

}
