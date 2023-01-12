package Lab_8;

import java.util.LinkedList;

public class CrawlerTask implements Runnable{
    //Задаем Глубину
    public URLDepthPair depthPair;
    //Задаем Пул
    public URLPool urlPool;
    //Конструктор, для установки Пула
    public CrawlerTask(URLPool pool){
        urlPool = pool;
    }
    //Метод запускает Задачи в CrawlerTask
    @Override
    public void run() {
        //Получаем следующю deptPair и пула
        depthPair = urlPool.get();
        // Гулбина depthPair
        int depth = depthPair.getDepth();
        // Получить все ссылки с сайта и сохранить их в новом связанном списке.
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList = Crawler.getAllLinks(depthPair);
        //System.out.println(depthPair + " " + linkedList);

        //Перебор ссылок сайта

        for (int i=0; i < linkedList.size(); i++){
            String newUrl = linkedList.get(i);
            //Создаем новую DepthPair для каждой ссылки и добавляем в пул
            URLDepthPair nDP = new URLDepthPair(newUrl, depth+1);

            urlPool.put(nDP);

        }
    }
}
