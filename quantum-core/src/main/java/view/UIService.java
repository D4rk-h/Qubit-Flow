package view;
import io.javalin.Javalin;
import java.nio.file.Paths;


public class UIService {
        public static void main(String[] args) {
            Javalin app = Javalin.create(config -> {
                String webRoot = Paths.get("www/public").toAbsolutePath().toString();
                config.staticFiles.add(webRoot, io.javalin.http.staticfiles.Location.EXTERNAL);
            }).start(7000);

            app.get("/", ctx -> ctx.redirect("/index.html"));
            app.get("/circuit", ctx -> ctx.redirect("/circuit.html"));

        }
}

