package pl.coderslab.starwarsapp.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    private final ObjectMapper om = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp,
                       org.springframework.security.access.AccessDeniedException ex) throws IOException {
        var pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        pd.setTitle("Forbidden");
        pd.setDetail("Forbidden access or session expired");
        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        resp.setContentType("application/problem+json");
        om.writeValue(resp.getOutputStream(), pd);
    }
}