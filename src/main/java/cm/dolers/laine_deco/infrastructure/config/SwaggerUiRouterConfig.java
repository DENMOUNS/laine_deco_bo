package cm.dolers.laine_deco.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Configuration pour servir Swagger UI sans dépendance aux ressources statiques
 */
@Configuration
public class SwaggerUiRouterConfig {

    @RestController
    public static class SwaggerUiController {
        
        @GetMapping("/swagger-ui.html")
        public void getSwaggerUiHtml(HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.getWriter().write(getSwaggerUiHtml());
        }
        
        @GetMapping("/swagger-ui-custom.html")
        public void getSwaggerUiCustomHtml(HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            response.getWriter().write(getSwaggerUiHtml());
        }
        
        @GetMapping("/swagger-ui/{path:.*}")
        public void getSwaggerResource(@PathVariable String path, HttpServletResponse response) throws IOException {
            serveResource(path, response);
        }
        
        private void serveResource(String path, HttpServletResponse response) {
            try {
                String resourcePath = path.replace("swagger-ui/", "");
                String fullPath = "META-INF/resources/webjars/swagger-ui/5.18.0/" + resourcePath;
                Resource resource = new ClassPathResource(fullPath);
                
                if (resource.exists()) {
                    byte[] content = resource.getInputStream().readAllBytes();
                    response.setContentType(parseContentType(resourcePath));
                    response.setContentLength(content.length);
                    try (OutputStream os = response.getOutputStream()) {
                        os.write(content);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        
        private String parseContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".svg")) return "image/svg+xml";
            if (path.endsWith(".woff")) return "font/woff";
            if (path.endsWith(".woff2")) return "font/woff2";
            return "text/plain";
        }
        
        private String getSwaggerUiHtml() {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Swagger UI</title>\n" +
                    "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/swagger-ui/swagger-ui.css\" />\n" +
                    "    <link rel=\"icon\" type=\"image/png\" href=\"/swagger-ui/favicon-32x32.png\" sizes=\"32x32\" />\n" +
                    "    <link rel=\"icon\" type=\"image/png\" href=\"/swagger-ui/favicon-16x16.png\" sizes=\"16x16\" />\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div id=\"swagger-ui\"></div>\n" +
                    "    <script src=\"/swagger-ui/swagger-ui-bundle.js\"></script>\n" +
                    "    <script src=\"/swagger-ui/swagger-ui-standalone-preset.js\"></script>\n" +
                    "    <script>\n" +
                    "        window.onload = function() {\n" +
                    "            window.ui = SwaggerUIBundle({\n" +
                    "                url: \"/v3/api-docs\",\n" +
                    "                dom_id: '#swagger-ui',\n" +
                    "                deepLinking: true,\n" +
                    "                presets: [\n" +
                    "                    SwaggerUIBundle.presets.apis,\n" +
                    "                    SwaggerUIBundle.presets.standalonePreset\n" +
                    "                ],\n" +
                    "                layout: \"StandaloneLayout\"\n" +
                    "            });\n" +
                    "        };\n" +
                    "    </script>\n" +
                    "</body>\n" +
                    "</html>";
        }
    }
}