package pl.coderslab.starwarsapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Wystawia aktualny CSRF token w nagłówku odpowiedzi (X-CSRF-TOKEN).
 * Dzięki temu SPA może go odczytać po GET-cie i użyć przy POST-ach.
 */
public class CsrfHeaderExposeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            response.setHeader("X-CSRF-TOKEN", token.getToken());
        }
        chain.doFilter(request, response);
    }
}
