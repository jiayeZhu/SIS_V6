package SISv6.Server;//package UpLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostQuery {
    public static void PostToPHP(String UrlIn, String QueryIn){
        HttpURLConnection conn=null;
        try{
        URL url=new URL(UrlIn);
        String agent="Applet";
        String query="query=" + QueryIn;
        String type="application/x-www-form-urlencoded";
        conn=(HttpURLConnection)url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty( "User-Agent", agent );
        conn.setRequestProperty( "Content-Type", type );
        conn.setRequestProperty( "Content-Length", ""+query.length());
        OutputStream out=conn.getOutputStream();
        out.write(query.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while((inputLine=in.readLine())!=null){
            //System.out.print(inputLine+"\n");
        }
        in.close();
        int grc = conn.getResponseCode();
        String grm=conn.getResponseMessage();
        }catch(Exception e){
        e.printStackTrace();
        }finally{
        conn.disconnect();
        }
    }
}