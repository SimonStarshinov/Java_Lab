package Lab_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Crawler {

    public static void main(String[] args) {

        // Поле для глубины поиска.
        int depth = 0;

        // Проверка на правильный ввод данных с терминала.
        if (args.length != 2) {
            System.out.println("usage: java Crawler <URL><depth>");
            System.exit(1);
        } else {
            try {
                depth = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("usage: java Crawler <URL><depth>");
                System.exit(1);
            }
        }

        // Создавамба списки (массивы, размер котороых можно изменять) для
        // обработанных URL и оставшихся URL.
        LinkedList<URLDepthPair> pendingURLs = new LinkedList<URLDepthPair>();
        LinkedList<URLDepthPair> processedURLs = new LinkedList<URLDepthPair>();

        URLDepthPair currentDepthPair = new URLDepthPair(args[0], 0);
        pendingURLs.add(currentDepthPair);

        // Просмотренные URL.
        ArrayList<String> seenURLs = new ArrayList<String>();
        seenURLs.add(currentDepthPair.getUrl());

        while (pendingURLs.size() != 0) {

            URLDepthPair depthPair = pendingURLs.pop();
            processedURLs.add(depthPair);
            int myDepth = depthPair.getDepth();

            // Засовываем все ссылки из сайта в список.
            LinkedList<String> linksList = new LinkedList<String>();
            linksList = Crawler.getAllLinks(depthPair);

            if (myDepth < depth) {
                // Пробегаемся по всем ссылкам в списке.
                for (int i = 0; i < linksList.size(); i++) {
                    String newURL = linksList.get(i);
                    // Проверка на увиденность.
                    if (seenURLs.contains(newURL)) {
                        continue;
                    } else {
                        URLDepthPair newDepthPair = new URLDepthPair(newURL, myDepth + 1);
                        pendingURLs.add(newDepthPair);
                        seenURLs.add(newURL);
                    }
                }
            }
        }
        // Выводим, собственно, все обработанные URL.
        Iterator<URLDepthPair> iter = processedURLs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    // Метод просматривает сайты и возвращает их в списке.
    private static LinkedList<String> getAllLinks(URLDepthPair myDepthPair) {

        // Спсиок для найденных URL.
        LinkedList<String> URLs = new LinkedList<String>();

        Socket sock;

        // Создаём сокет с портом 80, пытаемся подключиться к хосту.
        try {
            sock = new Socket(myDepthPair.getWebHost(), 80);
        }
        // UnknownHostException.
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            return URLs;
        }
        // IOException.
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return URLs;
        }

        // Ставим таймаут подключения сокета.
        try {
            sock.setSoTimeout(3000);
        }
        // SocketException.
        catch (SocketException exc) {
            System.err.println("SocketException: " + exc.getMessage());
            return URLs;
        }

        // Поля для рута и хоста URL.
        String docPath = myDepthPair.getPath();
        String webHost = myDepthPair.getWebHost();

        // Поле для OutputStream.
        OutputStream outStream;

        // Записываем OutputStream сокета в поле, созданное ранее.
        try {
            outStream = sock.getOutputStream();
        }
        // IOException.
        catch (IOException exce) {
            System.err.println("IOException: " + exce.getMessage());
            return URLs;
        }

        // PrintWriter будет автоматически выводить.
        PrintWriter myWriter = new PrintWriter(outStream, true);

        // Делаем запрос.
        myWriter.println("GET " + docPath + " HTTP/1.1");
        myWriter.println("Host: " + webHost);
        myWriter.println("Connection: close");
        myWriter.println();

        // Поле для InputStream.
        InputStream inStream;

        // Записываем InputStream сокета в поле, созданное ранее.
        try {
            inStream = sock.getInputStream();
        }
        // IOException..
        catch (IOException excep) {
            System.err.println("IOException: " + excep.getMessage());
            return URLs;
        }

        // Создаем InputStreamReader и BufferedReader для чтения сайта.
        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader BuffReader = new BufferedReader(inStreamReader);

        // Пытаемся прочесть одну строку сайта с помощью BufferedReader.
        while (true) {
            String line;
            try {
                line = BuffReader.readLine();
            }
            // IOException.
            catch (IOException except) {
                System.err.println("IOException: " + except.getMessage());
                return URLs;
            }
            // Если строк больше нет, то завершаем чтение.
            if (line == null)
                break;

            // Поля для индексов URL.
            int beginIndex = 0;
            int endIndex = 0;
            // Буферная переменная.
            int index = 0;

            while (true) {

                String URL_INDICATOR = "a href=\"";
                String END_URL = "\"";

                // Ищем начало URL в строке.
                index = line.indexOf(URL_INDICATOR, index);
                if (index == -1) // Если нет URL в строке, то как бы кирпич.
                    break;

                // Добавляем к индексу длину URL_INDICATOR, чтобы получить
                // индекс начала URL.
                index += URL_INDICATOR.length();
                beginIndex = index;

                // Ищем конец URL в строке и обновляем буферную переменную.
                endIndex = line.indexOf(END_URL, index);
                index = endIndex;

                // Режем URL и добавляем её в список URL.
                String newLink = line.substring(beginIndex, endIndex);
                URLs.add(newLink);
            }

        }

        return URLs;
    }

}
