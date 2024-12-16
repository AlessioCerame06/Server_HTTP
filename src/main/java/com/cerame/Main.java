package com.cerame;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while(true){
            Socket s = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String[] firstLine = in.readLine().split(" ");
            String method = firstLine[0];
            String resource = firstLine[1];
            String version = firstLine[2];
            System.out.println(method);
            System.out.println(resource);

            String header = "";
            do{
                header = in.readLine();
                System.out.println(header);
            }while(!header.isEmpty());
            System.out.println("fine");

            String response = "";
            if(resource.equals("/index.html")){
                response = "<html><body><b>BENVENUTO alla pagina principale</b></body></html>";
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Type: text/html\n");
                out.writeBytes("Content-Length: "+ response.length() +"\n");
                out.writeBytes("\n");
                out.writeBytes(response);
            }else{
                response = "Pagina non trovata";
                out.writeBytes("HTTP/1.1 404 Not found\n");
                out.writeBytes("Content-Type: text/plain\n");
                out.writeBytes("Content-Length: "+ response.length() +"\n");
                out.writeBytes("\n");
                out.writeBytes(response);
            }

            File file = new File("htdocs/index.html");
            if(file.exists()){
                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("Content-Type: image/png\n");
                out.writeBytes("\n");

                // legge un file, buttando fuori il contenuto di un file a blocchi di 8k
                InputStream input = new FileInputStream(file);
                byte[] buf = new byte[8192];
                int n;
                while((n = input.read(buf)) != -1){
                    out.write(buf, 0, n);
                }
                input.close();
            }
        }
    }
}