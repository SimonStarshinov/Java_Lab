package Lab_8;

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
        //Поле для кол-ва потоков
        int numThreads = 0;

        // Проверка на правильный ввод данных с терминала.
        if (args.length != 3) {
            System.out.println("usage: java Crawler <URL> <depth> <number of crawler threads");
            System.exit(1);
        } else {
            try {
                depth = Integer.parseInt(args[1]);
                numThreads = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("usage: java Crawler <URL> <depth> <number of crawler threads");
                System.exit(1);
            }
        }


        // Сайт, который ввел пользователь
        URLDepthPair currentDepthPair = new URLDepthPair(args[0], 0);
        //Создаем пул и добавляем сайт введеныый пользователем
        URLPool pool = new URLPool(depth);
        pool.put(currentDepthPair);

        //Переменная для начала потока
        //int totThreads = 0;
        int initActive = Thread.activeCount();
        //Пока ожидающие потоки не равны кол-во заданных потоков
        int timeout = 0;

        while (pool.getWaitingThreads() != numThreads) {
            //System.out.println(pool.getWaitingThreads() + " " + numThreads + " " + Thread.activeCount());
            //Если общее кол-во потоков меньше запрошенного, то создаем больше потоков и запускаем crawlerTask
            //Иначе спим

            if(Thread.activeCount() < numThreads){
                CrawlerTask crawler = new CrawlerTask(pool);
                new  Thread(crawler).start();
            }
            else {
                try {
                    Thread.sleep(100);
                    timeout++;
                } catch (InterruptedException e) {
                    System.out.println("Caught unexpected " +
                            "InterruptedException");
                }
                if (timeout > 50) break;
            }
        }
        //Когда закончили выводим все потоки и выходим из программы
        Iterator<URLDepthPair> iter = pool.proccesURLs.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }        // Exit.
        System.exit(0);
    }

    // Метод просматривает сайты и возвращает их в списке.
    public static LinkedList<String> getAllLinks(URLDepthPair myDepthPair) {

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
            System.err.println("1IOException: " + ex.getMessage());
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
            System.err.println("2IOException: " + exce.getMessage());
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
            System.err.println("3IOException: " + excep.getMessage());
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
                System.err.println("4IOException: " + except.getMessage());
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
