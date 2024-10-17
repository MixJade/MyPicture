import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.concurrent.Executors;

/**
 * Java的简易服务器
 *
 * @since 2024-10-01 20:09:39
 */
public class MyServer implements HttpHandler {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9527), 0);
        server.createContext("/public", new MyServer());

        server.setExecutor(Executors.newFixedThreadPool(1));
        server.start();
        System.out.println("服务器已启动");
        System.out.println("测试访问: http://localhost:9527/public/wallpaper/wp003.jpg");
        System.out.println("测试访问: http://localhost:9527/public/showhtml/svgdesktop.html?wp003.jpg");
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            // 从java的执行路径开始算
            String path = ".." + httpExchange.getRequestURI().getPath();
            System.out.println("访问" + path);
            File file = new File(path);
            if (file.isFile()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                // 为.svg后缀的请求设置响应头
                String httpFileName = httpExchange.getRequestURI().toString();
                if (httpFileName.endsWith(".svg")) {
                    httpExchange.getResponseHeaders().add("Content-Type", "image/svg+xml");
                } else if (httpFileName.endsWith(".js")) {
                    httpExchange.getResponseHeaders().add("Content-Type", "application/javascript; charset=utf-8");
                } else if (httpFileName.endsWith(".css")) {
                    httpExchange.getResponseHeaders().add("Content-Type", "text/css; charset=utf-8");
                }
                httpExchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                String response = "File not found.";
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}