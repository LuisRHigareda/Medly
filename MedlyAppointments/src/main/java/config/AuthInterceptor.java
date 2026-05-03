package config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 
 * @author Leonardo Flores Leyva
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. Obtenemos la sesión actual. 'false' significa que no cree una nueva si no existe.
        HttpSession session = request.getSession(false);
        // 2. Verificamos si la sesión existe y si el atributo "user" está presente.
        if (session != null && session.getAttribute("user") != null) {
            // El usuario está logueado. Retornamos 'true' para dejar pasar la petición al controlador.
            return true;
        }
        // 3. Si llegamos aquí, el usuario no está logueado.
        // Hacemos la redirección manualmente desde el objeto 'response'.
        response.sendRedirect("/index?error=user_not_logged_in");
        // Retornamos 'false' para abortar la petición y que el controlador NUNCA se ejecute.
        return false;
    }
}