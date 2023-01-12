package Lab_7;

import java.net.*;

public class URLDepthPair {
    private String cur_url;
    private int cur_depth;


    public URLDepthPair(String url, int depth){
        cur_url = url;
        cur_depth = depth;
    }

    public int getDepth() {
        return cur_depth;
    }

    public String getUrl() {
        return cur_url;
    }


    @Override
    public String toString() {
        String stringDepth = Integer.toString(cur_depth);
        return cur_url + ' ' + stringDepth;
    }
//Возвращает хост URL.
    public String getWebHost(){
        try {
            URL url = new URL(cur_url);
            return url.getHost();
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException" + e.getMessage());
            return null;
        }
    }
//Возвращает путь URL.
    public String getPath(){
        try {
            URL url = new URL(cur_url);
            return url.getPath();
        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException" + e.getMessage());
            return null;
        }
    }


}
