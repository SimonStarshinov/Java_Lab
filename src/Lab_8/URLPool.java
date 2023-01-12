package Lab_8;

import java.util.*;
public class URLPool {
    private final int maxDepth;
    //Список юрл в ожидании
   private LinkedList<URLDepthPair> pendingURLs;
    //Список обработанных юрл
   public LinkedList<URLDepthPair> proccesURLs;
   //Список просматриванных

   private  ArrayList<String> seenURL = new ArrayList<String>();
   // Поле отслеживание количества ожидающих потоков

   public int waitingThreads;
    //В конструкторе инициализируем поля Списка Юрл в ожидании, списка обработанных и кол-во ожидающих
    //потоков
   public URLPool(int maxDepth){
       this.maxDepth = maxDepth;
       waitingThreads = 0;
       pendingURLs = new LinkedList<URLDepthPair>();
       proccesURLs = new LinkedList<URLDepthPair>();
   }
   //Синхронизированный метод получет кол-во ожидающих потоков
   public synchronized int getWaitingThreads(){
       return waitingThreads;
   }
    // Возращает размер пула
   public synchronized int size(){
       return pendingURLs.size();
   }

    //Добавляет URLDepthPair в пул
   public synchronized boolean put(URLDepthPair depthPair){
       //Была ли добавлена глубина?
       boolean added = false;
       //Если глубина меньше максимальной добавляем DepthPair в пул
       if(depthPair.getDepth() < maxDepth){
           //System.out.println("URLPOOL" + depthPair.getDepth());
           pendingURLs.addLast(depthPair);
           added = true;
           //Уменьшая кол-во потоков notify - продолжает поток
           waitingThreads--;
           this.notify();
       }
       //Если глубина >= максимальной, то просто добавялкм в просмотренный список
       else {
           seenURL.add(depthPair.getUrl());
       }
       //Возращаем added (был добавлен или нет)
       return added;
   }

   public synchronized URLDepthPair get(){
       //Устанавливаем URLDepthPair null
       URLDepthPair mdp = null;

       //Пока пул пустой ждем
       //System.out.println(pendingURLs);
       if (pendingURLs.size() == 0){
           waitingThreads++;
           try {
               this.wait();
           }
           catch (InterruptedException e){
               System.err.println("MalformedURLException: " + e.getMessage());
               return null;
           }
       }
       //Удаляем первую пару из просмотренных, и добавляем в обработанные
       //Потом возращаем
       mdp = pendingURLs.removeFirst();
       seenURL.add(mdp.getUrl());
       proccesURLs.add(mdp);
       return mdp;
   }
   //Получить список просмотренных URL-адремов
   public synchronized ArrayList<String> getSeenURL(){
       return seenURL;
   }
}
