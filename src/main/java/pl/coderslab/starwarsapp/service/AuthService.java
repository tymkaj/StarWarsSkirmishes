package pl.coderslab.starwarsapp.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Service;
import pl.coderslab.starwarsapp.requests.LoginRequest;
import pl.coderslab.starwarsapp.requests.RegisterRequest;
import pl.coderslab.starwarsapp.entity.User;
import pl.coderslab.starwarsapp.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CsrfTokenRepository csrfTokenRepository;

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "username taken"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "email taken"));
        }

        String pw = request.getPassword();
        if (!pw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&()_+\\-={}\\[\\]\\\\|:;\"'<>,./]).{8,}$")) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "password must have at least 8 characters, an uppercase and lowercase and a special character"));
        }

        User u = new User();
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setPassword(passwordEncoder.encode(pw));
        userRepository.save(u);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> login(LoginRequest request,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {

        var userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Invalid login or password"));
        }

        // 1) build Authentication
        var auth = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // 2) Save to SecurityContext (in session)
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        new HttpSessionSecurityContextRepository()
                .saveContext(context, httpRequest, httpResponse);

        // 3) Session ID migration - to avoid session fixation
        httpRequest.changeSessionId();

        // 4) Additional attributes
        HttpSession session = httpRequest.getSession(false); // po changeSessionId nadal istnieje
        if (session != null) {
            session.setAttribute("username", request.getUsername());
            session.setAttribute("userId", userOpt.get().getId());
        }

        csrfTokenRepository.saveToken(null, httpRequest, httpResponse);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        var cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Map<String, Object>> sessionInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = auth.getName();
        var user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "username", username,
                "userId", user.getId()
        ));
    }


}
