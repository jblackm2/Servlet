package com.first;

import com.first.Host_Gather;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by blackmju on 7/21/2015.
 */
public class First {
    static boolean flag = false;
    static ArrayList<String> host_list = new ArrayList<>();
    static ArrayList<String> PROD_host_list = new ArrayList<>();

    public static void main() {

        //Select the number of threads desired
        File file = new File("C:\\Users\\blackmju\\Documents\\HostName.txt");
        File PROD_file = new File("C:\\Users\\blackmju\\Documents\\PROD_HostName.txt");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        String Host_url = "http://orddnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=gethoststatus&username=guest&ticket=tevint8kjejbvoi9rn797bkoansgqvvlbt52odjbq6lot50np8tvihnp8i7vh7ir";
        String Service_url = "http://orddnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=getservicestatus&username=guest&ticket=tevint8kjejbvoi9rn797bkoansgqvvlbt52odjbq6lot50np8tvihnp8i7vh7ir";
        String PROD_Host_url = "http://ordpnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=gethoststatus&username=guest&ticket=n46qoujglff9tvd557kiiqkjtmqesgfjhkb8hul4b4d62na4rtldmatjtovp6ebk";
        String PROD_Service_url = "http://ordpnocnag01.ord.dsghost.net/nagiosxi/backend/?cmd=getservicestatus&username=guest&ticket=n46qoujglff9tvd557kiiqkjtmqesgfjhkb8hul4b4d62na4rtldmatjtovp6ebk";

/*
        Runnable Host_status_task = new Runnable() {


            @Override
            public void run() {
                Host_Gather.gather(Host_url, executor, host_list);
                Host_Gather.gather(PROD_Host_url, executor, PROD_host_list);
            }

        };
        Runnable Service_status_task = new Runnable() {
            @Override
            public void run() {
                Service_Gather.gather(Service_url, executor, host_list);
                Service_Gather.gather(PROD_Service_url, executor, PROD_host_list);
            }

        };
        Runnable Read_file_task = new Runnable() {
            @Override
            public void run() {

                host_list = ReadFile.get_host_names(file);

                ArrayList<String> remove_list = new ArrayList<>();
                String REGEX = "(\\w+)(\\s*:\\s*)(\\w+.*\\w+)*(.com)+(\\s*)";
                for(int i = 0; i< host_list.size(); i++){
                    if(host_list.get(i).matches(REGEX)){
                        //System.out.println("True: " + i);
                    }
                    else{
                        //System.out.println("False: " + i);
                        remove_list.add(host_list.get(i));
                    }
                }
                //System.out.println(remove_list);
                //System.out.println("Host size: " + host_list.size());
                host_list.removeAll(remove_list);
                //System.out.println("Host size: " + host_list.size());
            }

        };
        Runnable Read_PROD_file_task = new Runnable() {
            @Override
            public void run() {

                PROD_host_list = ReadFile.get_host_names(PROD_file);

                ArrayList<String> remove_list = new ArrayList<>();
                String REGEX = "(\\w+)(\\s*:\\s*)(\\w+.*\\w+)*(.com)+(\\s*)";
                for(int i = 0; i< PROD_host_list.size(); i++){
                    if(PROD_host_list.get(i).matches(REGEX)){
                        //System.out.println("True: " + i);
                    }
                    else{
                        //System.out.println("False: " + i);
                        remove_list.add(PROD_host_list.get(i));
                    }
                }
                //System.out.println(remove_list);
                //System.out.println("Host size: " + host_list.size());
                PROD_host_list.removeAll(remove_list);
                // System.out.println("Host size: " + host_list.size());
            }

        };


        //executor.scheduleWithFixedDelay(Read_file_task, 1, 360, TimeUnit.SECONDS);//Interval to read file
        //executor.scheduleWithFixedDelay(Read_PROD_file_task, 2, 60, TimeUnit.SECONDS);//Interval to read file
        //executor.scheduleWithFixedDelay(Host_status_task, 10, 10, TimeUnit.SECONDS);//Interval to execute Host_status
        //executor.scheduleWithFixedDelay(Service_status_task, 5, 60, TimeUnit.SECONDS);//Interval to execute Service_status


        return "Finished";
    }


    // Called when there is an exception in Host_gather/Service_Gather
    public static void shutdown(ScheduledExecutorService e) {
        e.shutdown();
        System.out.println("Is ScheduledThreadPool shutting down? " + e.isShutdown());

    }

*/
    }

}

