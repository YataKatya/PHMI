/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

//import com.sun.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Пользователь
 */
public class Client {

    static String protocol = "http://";
    static String host = "localhost";
    static int port = 8080;

    // public static NewJFrame x1;
    static NewJFrame x1;
    //Методы
    static String ping = "/Ping";
    static String stop = "/Stop";
    //для клиента
    static String getinput = "/GetInputData";
    static Input get1;
    static boolean get1b = false;
    static String writeanswer = "/WriteAnswer";
    static Output get11;
    //для самого сервера
    static String postinput = "/PostInputData";
    static Input post1;
    static boolean post1b = false;
    static String getanswer = "/GetAnswer";
    static Output post11;
    static String USER_AGENT = "Mozilla/5.0";

    private static void pingOrStop(String urlString) throws Exception {
        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        //Считываем тело ответа    
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\r\n");
        }
        in.close();

        System.out.println(response.toString());

        //Считываем тело ответа
        int beg = 0;
        StringBuffer response_body = new StringBuffer();
        if (response.indexOf("\r\n\r\n") != -1) {
            beg = response.indexOf("\r\n\r\n");
            response_body.append(response.substring(beg + 4));
        }

        Client.update("\r\n" + con.getRequestMethod() + "  " + obj.getProtocol() + "  " + obj.toURI() + "\r\n" + response_body + "\r\n", response.toString());
        con.disconnect();
    }

    private static void getInput(String urlString) throws Exception {
        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        //Считываем тело ответа    
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\r\n");
        }
        in.close();

        Client.update("\r\n" + con.getRequestMethod() + "  " + obj.getProtocol() + "  " + obj.toURI() + "\r\n", response.toString()); //+"\r\n" 
        System.out.println(response.toString());

        //Считываем тело ответа
        int beg = 0;
        StringBuffer response_body = new StringBuffer();
        if (response.indexOf("\r\n\r\n") != -1) {
            beg = response.indexOf("\r\n\r\n");
            response_body.append(response.substring(beg + 4));
        }
        // JOptionPane.showMessageDialog(null, "Тело ответа:  "+response_body.toString());

        get1 = new Input("json", response_body.toString());
        get1b = true;

        con.disconnect();
    }

    private static void writeAnswer(String urlString) throws Exception {
        if (get1b) {
            get11 = new Output(get1);
            byte[] data = null;
            String url_body = new String("\r\n\r\n" + get11.Respect("json"));

            URL obj = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);

            //add reuqest header
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Length", "" + Integer.toString(url_body.getBytes().length));

            OutputStream os = con.getOutputStream();
            data = url_body.getBytes("UTF-8");
            os.write(data);
            data = null;

            con.connect();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + urlString);
            System.out.println("Post body : " + url_body);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            response.append("\r\n");
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\r\n");
            }
            in.close();

            System.out.println(response.toString());

            //Считываем тело ответа
            int beg = 0;
            StringBuffer response_body = new StringBuffer();
            if (response.indexOf("\r\n\r\n") != -1) {
                beg = response.indexOf("\r\n\r\n");
                response_body.append(response.substring(beg + 4));
            }

            //print result
            System.out.println(response.toString());
            Client.update("\r\n" + con.getRequestMethod() + "  " + obj.getProtocol() + "  " + obj.toURI() + "\r\n" + url_body + "\r\n", response.toString());

        } else {
            JOptionPane.showMessageDialog(null, "Вы еще не запрашивали входные данные для задачи (метод /GetInput).");
        }
        get1b = false;
    }

    private static void postInput(String urlString) throws Exception {
        post1 = new Input();
        byte[] data = null;
        String url_body = new String("\r\n\r\n" + post1.Request("json"));

        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);

        //add reuqest header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Length", "" + Integer.toString(url_body.getBytes().length));

        OutputStream os = con.getOutputStream();
        data = url_body.getBytes("UTF-8");
        os.write(data);
        data = null;

        con.connect();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + urlString);
        System.out.println("Post body : " + url_body);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        response.append("\r\n");
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\r\n");
        }
        in.close();

        System.out.println(response.toString());

        //Считываем тело ответа
        int beg = 0;
        StringBuffer response_body = new StringBuffer();
        if (response.indexOf("\r\n\r\n") != -1) {
            beg = response.indexOf("\r\n\r\n");
            response_body.append(response.substring(beg + 4));
        }

        //print result
        System.out.println(response.toString());
        Client.update("\r\n" + con.getRequestMethod() + "  " + obj.getProtocol() + "  " + obj.toURI() + "\r\n" + url_body + "\r\n", response.toString());

        get1b = false;
    }

    private static void getAnswer(String urlString) throws Exception {
        URL obj = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        //Считываем тело ответа    
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\r\n");
        }
        in.close();

        Client.update("\r\n" + con.getRequestMethod() + "  " + obj.getProtocol() + "  " + obj.toURI() + "\r\n", response.toString()); //+"\r\n" 
        System.out.println(response.toString());

        //Считываем тело ответа
        int beg = 0;
        StringBuffer response_body = new StringBuffer();
        if (response.indexOf("\r\n\r\n") != -1) {
            beg = response.indexOf("\r\n\r\n");
            response_body.append(response.substring(beg + 4));
        }

        post11 = new Output("json", response_body.toString());
        post1b = true;

        con.disconnect();
    }

    public static void send(String method) throws Exception {
        port = Inner_server.port;

        // создаем объект который отображает вышеописанный IP-адрес.
        String urlString = protocol + host + ":" + port + method;
        
        //Это пинг или стоп - простые GET Запросы
        if ((method.equalsIgnoreCase(ping) || (method.equalsIgnoreCase(stop)))) {
            pingOrStop(urlString);
        }

        // Прием входных данных - GetInputData       
        if (method.equalsIgnoreCase(getinput)) {
            getInput(urlString);
        }

        // Отдача ответа по задаче - выходных данных - WriteAnswer      
        if (method.equalsIgnoreCase(writeanswer)) {
            writeAnswer(urlString);
        }

        // Отправка входных данных по задаче - PostInputData      
        if (method.equalsIgnoreCase(postinput)) {
            postInput(urlString);
        }

        // Прием ответа по задаче - GetAnswer      
        if (method.equalsIgnoreCase(getanswer)) {
            getAnswer(urlString);
        }
    }

    static void update(String reqs, String resp) {
        x1.update_client(reqs, resp);
    }

}
