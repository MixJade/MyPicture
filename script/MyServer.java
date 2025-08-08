import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Java的简易服务器
 *
 * @since 2024-10-01 20:09:39
 */
public class MyServer implements HttpHandler {
    private static final String REQ_CONTEXT = "/public";
    private static final String REQ_FOLDER = "..";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9527), 0);
        server.createContext(REQ_CONTEXT, new MyServer());

        server.setExecutor(Executors.newFixedThreadPool(1));
        server.start();
        System.out.println("服务器已启动");
        System.out.println("测试访问:http://localhost:9527" + REQ_CONTEXT + "/wallpaper/wp001.jpg");
        System.out.println("测试访问:http://localhost:9527" + REQ_CONTEXT + "/assert/svgdesktop.html");
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            // 另一个执行器(此处额外逻辑)
            if ((REQ_CONTEXT + "/list").equals(httpExchange.getRequestURI().getPath())) {
                String response = getImgFileList();
                httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
                // 获取字节数组时明确指定UTF-8编码
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                // 使用正确编码的字节长度
                httpExchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
            // 从java的执行路径开始算
            String path;
            // 特殊的页面逻辑
            if ((httpExchange.getRequestURI().getPath()).startsWith(REQ_CONTEXT + "/assert")) {
                path = httpExchange.getRequestURI().getPath().substring(REQ_CONTEXT.length() + 1);
            } else {
                path = REQ_FOLDER + httpExchange.getRequestURI().getPath();
            }
            System.out.println("request--> " + path);
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

    /**
     * 输出目标文件夹下的图片列表
     */
    private String getImgFileList() {
        // 指定要读取的文件夹路径
        File folder = new File(REQ_FOLDER + REQ_CONTEXT + "/wallpaper");
        List<String> fileNames = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            // 输出文件或文件夹名称
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jpg")) fileNames.add(file.getName());
                }
            } else System.out.println("该文件夹为空。");
        } else System.out.println("指定的路径不存在或者不是一个有效的文件夹。");
        return String.join(",", fileNames);
    }
}