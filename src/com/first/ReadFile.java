package com.first;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by blackmju on 7/27/2015.
 */
public class ReadFile {

    public static ArrayList<String> get_host_names(File file) {

        StringBuilder sb = new StringBuilder();
        ArrayList<String> host_list = new ArrayList<>();

        ArrayList<String[]> Host_list = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String text_line = null;
        String [] test= null;

        try {
            while ((text_line = br.readLine()) != null && text_line.length() != 0) {
                sb.append(text_line);
                host_list.add(text_line);
                //test = text_line.split("\\s*:\\s*");
                //host_list.add(test);
                //System.out.println(test[0] + test[1]);
                //Host_list.add(test);
                //host_table.put(test[1], test[0]);

            }

            br.close();

            /*for(int i = 0; i < Host_list.size(); i ++){
                System.out.println(Host_list.get(i));
            }


            for(int i = 0; i < host_list.size(); i ++){
                System.out.println(i + host_list.get(i));
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    return host_list;

    }
}
