package com.first;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * Created by blackmju on 8/25/2015.
 */
public class Servlet extends javax.servlet.http.HttpServlet {

    private final File file = new File("C:\\Users\\blackmju\\Documents\\HostName.txt");
    private final File PROD_file = new File("C:\\Users\\blackmju\\Documents\\PROD_HostName.txt");

    static ArrayList<String> host_list = new ArrayList<>();
    static ArrayList<String> PROD_host_list = new ArrayList<>();

    private final static String Host_url = "http://orddnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=gethoststatus&username=guest&ticket=tevint8kjejbvoi9rn797bkoansgqvvlbt52odjbq6lot50np8tvihnp8i7vh7ir";
    private final static String Service_url = "http://orddnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=getservicestatus&username=guest&ticket=tevint8kjejbvoi9rn797bkoansgqvvlbt52odjbq6lot50np8tvihnp8i7vh7ir";
    private final static String PROD_Host_url = "http://ordpnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=gethoststatus&username=guest&ticket=n46qoujglff9tvd557kiiqkjtmqesgfjhkb8hul4b4d62na4rtldmatjtovp6ebk";
    private final static String PROD_Service_url = "http://ordpnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=getservicestatus&username=guest&ticket=n46qoujglff9tvd557kiiqkjtmqesgfjhkb8hul4b4d62na4rtldmatjtovp6ebk";


    static JSONObject host_message;
    static JSONObject service_message;
    static JSONObject message;
    Queue<JSONObject> queue = new CircularFifoQueue<>(2);

    public void init() throws ServletException
    {
        // Do required initialization

        host_list = ReadFile.get_host_names(file);

        ArrayList<String> remove_list = new ArrayList<>();
        //String REGEX = "(\\w+)(\\s*:\\s*)(\\w+.*\\w+)*(.com)+(\\s*)"; Original REGEX
        String REGEX = "(\\w+)(\\s*:\\s*)(\\w+)(\\s*:\\s*)(\\w+.*\\w+)*(.com)+(\\s*)";
        for(int i = 0; i< host_list.size(); i++){
            if(host_list.get(i).matches(REGEX)){
                //System.out.println("True: " + i);
            }
            else{
                //System.out.println("False: " + i);
                remove_list.add(host_list.get(i));
            }
        }

        host_list.removeAll(remove_list);
        remove_list.clear();

        PROD_host_list = ReadFile.get_host_names(PROD_file);

        //ArrayList<String> remove_list = new ArrayList<>();
        //String REGEX = "(\\w+)(\\s*:\\s*)(\\w+.*\\w+)*(.com)+(\\s*)";
        for(int i = 0; i< PROD_host_list.size(); i++){
            if(PROD_host_list.get(i).matches(REGEX)){
                //System.out.println("True: " + i);
            }
            else{
                //System.out.println("False: " + i);
                remove_list.add(PROD_host_list.get(i));
            }
        }
        PROD_host_list.removeAll(remove_list);
        System.out.println("Host size: " + host_list.size());
        System.out.println("PROD Host size: " + PROD_host_list.size());

        int initDelay = 0;
        int period = 300000;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                doGather(Host_url, host_list, PROD_Host_url, PROD_host_list, Service_url, PROD_Service_url);
            }
        };
        timer.scheduleAtFixedRate(task, initDelay, period);



    }

    public void doGather(String Host_url, ArrayList<String> host_list, String PROD_Host_url, ArrayList<String> PROD_host_list, String Service_url, String PROD_Service_url){


        Host_Gather.gather(Host_url, host_list);
        host_message = Host_Gather.gather(PROD_Host_url, PROD_host_list);
        System.out.println("Host Message: " + host_message);

        Service_Gather.Test_list.clear();

        Service_Gather.gather(Service_url, host_list);
        service_message = Service_Gather.gather(PROD_Service_url, PROD_host_list);
        queue.add(service_message);

        System.out.println("Queue Contents1: " + queue);
        System.out.println("Queue Size1: " + queue.size());
        System.out.println("Service Message: " + service_message);


    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        System.out.println("DATA :" + data);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:8000");
        response.addHeader("Access-Control-Allow-Credentials", "true");

        if(data.contains("host")){
            //Host_Gather.gather(Host_url, host_list);
            //message = Host_Gather.gather(PROD_Host_url, PROD_host_list);
            //message = host_message;
            System.out.println("Host: " + host_message);
            PrintWriter out = response.getWriter();
            out.print(host_message);
            out.flush();
            out.close();
        }
        else if(data.contains("service")){
            message = queue.element();

            System.out.println("Service: " + message);
            System.out.println("Queue Contents2: " + queue);
            System.out.println("Queue Size2: " + queue.size());


            PrintWriter out = response.getWriter();
            out.print(message);
            out.flush();
            out.close();
            //Service_Gather.gather(Service_url, host_list);
            //message = Service_Gather.gather(PROD_Service_url, PROD_host_list);
            //message = service_message;

        }




        // Actual logic goes here.
        //PrintWriter out = response.getWriter();
        //out.print(message);
        //out.flush();
        //message.clear();
        //doGet(request, response);


    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        //String command = request.getParameter("msg");
       // if (command.equals("host")){
           // System.out.println("Command: " + command);

        //}


        // Set response content type
        //request.setCharacterEncoding("utf8");
        /*if(request.equals("localhost:8080/Servlet")){
            response.setContentType("text/html");

            // Actual logic goes here.
            PrintWriter out = response.getWriter();
            out.println("<h1>" + message + "</h1>");
            //First.main();

        }*/

        //response.setContentType("text/html");
        //PrintWriter out = response.getWriter();
        //out.println(message);

    }

    public void destroy()
    {
        // do nothing.
    }



}
