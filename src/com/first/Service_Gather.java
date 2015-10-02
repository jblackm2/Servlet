package com.first;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by blackmju on 7/23/2015.
 */
public class Service_Gather {

    static ArrayList<String> DIT_host_list = new ArrayList<>();
    static ArrayList<String> PERF_host_list = new ArrayList<>();
    static ArrayList<String> Stage_host_list = new ArrayList<>();
    static ArrayList<String> PROD_host_list = new ArrayList<>();
    static ArrayList<String> Auth_host_list = new ArrayList<>();
    static ArrayList<String> CA_host_list = new ArrayList<>();
    static ArrayList<String> EIS_host_list = new ArrayList<>();
    static ArrayList<String> IS_host_list = new ArrayList<>();
    static ArrayList<String> PIS_host_list = new ArrayList<>();
    static ArrayList<String> Polling_host_list = new ArrayList<>();
    static ArrayList<String> Test_host_list = new ArrayList<>();
    static Hashtable<String, String> Env_group_table = new Hashtable<>();
    static Hashtable<String, String> Env_Auth_table = new Hashtable<>();
    static Hashtable<String, String> Env_CA_table = new Hashtable<>();
    static Hashtable<String, String> Env_EIS_table = new Hashtable<>();
    static Hashtable<String, String> Env_IS_table = new Hashtable<>();
    static Hashtable<String, String> Env_PIS_table = new Hashtable<>();
    static Hashtable<String, String> Env_Polling_table = new Hashtable<>();
    static Hashtable<String, String> Group_flag_table = new Hashtable<>();
    static Hashtable<String, String> Individual_DIT_table = new Hashtable<>();
    static Hashtable<String, String> Individual_PERF_table = new Hashtable<>();
    static Hashtable<String, String> Individual_Stage_table = new Hashtable<>();
    static Hashtable<String, String> Individual_PROD_table = new Hashtable<>();
    static Hashtable<String, String> Individual_flag_table = new Hashtable<>();
    static Hashtable<String, String> Individual_Auth_table = new Hashtable<>();
    static Hashtable<String, String> Individual_CA_table = new Hashtable<>();
    static Hashtable<String, String> Individual_EIS_table = new Hashtable<>();
    static Hashtable<String, String> Individual_IS_table = new Hashtable<>();
    static Hashtable<String, String> Individual_PIS_table = new Hashtable<>();
    static Hashtable<String, String> Individual_Polling_table = new Hashtable<>();
    static ArrayList<String> New_host_list = new ArrayList<>();
    static ArrayList<String> Final_host_list = new ArrayList<>();
    static List<String[]> service_okay_list = new ArrayList<String[]>();
    static List<String[]> service_warning_list = new ArrayList<String[]>();
    static List<String[]> service_critical_list = new ArrayList<String[]>();
    static Boolean clear_flag = false;

    static ArrayList<JSONObject> Test_list = new ArrayList<>();
    static JSONObject obj8 = new JSONObject();

    public static JSONObject gather(String service_url, ArrayList<String> host_list){

        System.out.println(Thread.currentThread().getName() + " Service task");

        String [] temp = null;
        JSONObject obj7 = new JSONObject();


        for(int i = 0; i< host_list.size(); i++){
            temp = host_list.get(i).split("\\s*:\\s*");
            //System.out.println(temp[0] + temp[1]);
            if(!Final_host_list.contains(temp[2])){
                New_host_list.add(temp[2]);
                Final_host_list.add(temp[1]);
                Final_host_list.add(temp[2]);
            }
            if (temp[0].equals("DIT")&& !DIT_host_list.contains(temp[2])){
                DIT_host_list.add(temp[2]);
            }
            else if (temp[0].equals("PERF")&& !PERF_host_list.contains(temp[2])){
                PERF_host_list.add(temp[2]);
            }
            else if (temp[0].equals("Stage")&& !Stage_host_list.contains(temp[2])){
                Stage_host_list.add(temp[2]);
            }
            else if (temp[0].equals("PROD")&& !PROD_host_list.contains(temp[2])){
                PROD_host_list.add(temp[2]);
            }

            if (temp[1].equals("Auth") && !Auth_host_list.contains(temp[2])){
                Auth_host_list.add(temp[2]);
            }
            else if(temp[1].equals("CA") && !CA_host_list.contains(temp[2])){
                CA_host_list.add(temp[2]);
            }
            else if(temp[1].equals("EIS") && !EIS_host_list.contains(temp[2])){
                EIS_host_list.add(temp[2]);
            }
            else if(temp[1].equals("IS") && !IS_host_list.contains(temp[2])){
                IS_host_list.add(temp[2]);
            }
            else if(temp[1].equals("PIS") && !PIS_host_list.contains(temp[2])){
                PIS_host_list.add(temp[2]);
            }
            else if(temp[1].equals("Polling") && !Polling_host_list.contains(temp[2])){
                Polling_host_list.add(temp[2]);
            }
            else if(temp[1].contains("Test") && !Test_host_list.contains(temp[2])){
                Test_host_list.add(temp[2]);
            }
            else{
                //System.out.println("Not a valid host_list");
            }

        }
        //print_lists();



        try {
            URL url = new URL(service_url);
            URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            doc.getDocumentElement().normalize();
            String name = doc.getDocumentElement().getNodeName();
            System.out.println(name);

            NodeList list = doc.getElementsByTagName("servicestatus");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                //System.out.println("Node: " + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");

                    String host_name = element.getElementsByTagName("host_name").item(0).getTextContent();
                    if(New_host_list.contains(host_name)) {

                        String a = element.getElementsByTagName("current_state").item(0).getTextContent();
                        if (a.equals("0")) {
                            String[] object = new String[10];
                            object[0] = (element.getElementsByTagName("name").item(0).getTextContent());
                            object[1] = (element.getElementsByTagName("host_id").item(0).getTextContent());
                            object[2] = (element.getElementsByTagName("status_text").item(0).getTextContent());
                            object[3] = (element.getElementsByTagName("host_name").item(0).getTextContent());
                            object[4] = (element.getElementsByTagName("current_state").item(0).getTextContent());
                            object[5] = (element.getElementsByTagName("status_update_time").item(0).getTextContent());
                            object[6] = (element.getElementsByTagName("current_check_attempt").item(0).getTextContent());
                            object[7] = (element.getElementsByTagName("max_check_attempts").item(0).getTextContent());
                            object[8] = (element.getElementsByTagName("check_command").item(0).getTextContent());
                            if(object[0].startsWith("HTTP - ")){
                                object[0] = object[0].replace("HTTP - ", "");
                            }
                            service_okay_list.add(object);
                            Individual_flag_table.put(object[3], "green");
                            JSONObject t = new JSONObject();
                            String u = new String();
                            t.put("host_name", object[3]);
                            t.put("name", object[0]);
                            t.put("status", object[2]);
                            t.put("current_state", object[4]);
                            t.put("status_update_time", object[5]);
                            t.put("current_check_attempt", object[6]);
                            t.put("max_check_attempts", object[7]);
                            u = getUrl(object);

                            t.put("url", u);
                            Test_list.add(t);
                        }
                        else if (a.equals("1")) {
                            String[] object = new String[10];
                            object[0] = (element.getElementsByTagName("name").item(0).getTextContent());
                            object[1] = (element.getElementsByTagName("host_id").item(0).getTextContent());
                            object[2] = (element.getElementsByTagName("status_text").item(0).getTextContent());
                            object[3] = (element.getElementsByTagName("host_name").item(0).getTextContent());
                            object[4] = (element.getElementsByTagName("current_state").item(0).getTextContent());
                            object[5] = (element.getElementsByTagName("status_update_time").item(0).getTextContent());
                            object[6] = (element.getElementsByTagName("current_check_attempt").item(0).getTextContent());
                            object[7] = (element.getElementsByTagName("max_check_attempts").item(0).getTextContent());
                            object[8] = (element.getElementsByTagName("check_command").item(0).getTextContent());
                            if(object[0].startsWith("HTTP - ")){
                                object[0] = object[0].replace("HTTP - ", "");
                            }
                            service_warning_list.add(object);
                            Individual_flag_table.put(object[3], "yellow");
                            JSONObject t = new JSONObject();
                            String u = new String();
                            t.put("host_name", object[3]);
                            t.put("name", object[0]);
                            t.put("status", object[2]);
                            t.put("current_state", object[4]);
                            t.put("status_update_time", object[5]);
                            t.put("current_check_attempt", object[6]);
                            t.put("max_check_attempts", object[7]);

                            u = getUrl(object);
                            t.put("url", u);
                            Test_list.add(t);
                        }
                        else if (a.equals("2")) {
                            String[] object = new String[10];
                            //test
                            object[0] = (element.getElementsByTagName("name").item(0).getTextContent());
                            object[1] = (element.getElementsByTagName("host_id").item(0).getTextContent());
                            object[2] = (element.getElementsByTagName("status_text").item(0).getTextContent());
                            object[3] = (element.getElementsByTagName("host_name").item(0).getTextContent());
                            object[4] = (element.getElementsByTagName("current_state").item(0).getTextContent());
                            object[5] = (element.getElementsByTagName("status_update_time").item(0).getTextContent());
                            object[6] = (element.getElementsByTagName("current_check_attempt").item(0).getTextContent());
                            object[7] = (element.getElementsByTagName("max_check_attempts").item(0).getTextContent());
                            object[8] = (element.getElementsByTagName("check_command").item(0).getTextContent());
                            if(object[0].startsWith("HTTP - ")){
                                object[0] = object[0].replace("HTTP - ", "");
                            }
                            service_critical_list.add(object);
                            Individual_flag_table.put(object[3], "red");
                            JSONObject t = new JSONObject();
                            String u = new String();
                            t.put("host_name", object[3]);
                            t.put("name", object[0]);
                            t.put("status", object[2]);
                            t.put("current_state", object[4]);
                            t.put("status_update_time", object[5]);
                            t.put("current_check_attempt", object[6]);
                            t.put("max_check_attempts", object[7]);

                            u = getUrl(object);
                            t.put("url", u);
                            Test_list.add(t);
                        }


                            /* Test prints
                            System.out.println("Host name: " + id);
                            System.out.println(element.getElementsByTagName("host_name").item(0).getTextContent());
                            System.out.println(element.getElementsByTagName("status_text").item(0).getTextContent());

                            System.out.print(element.getElementsByTagName("current_check_attempt").item(0).getTextContent() + "/");
                            System.out.println(element.getElementsByTagName("max_check_attempts").item(0).getTextContent());
                            */
                    }
                }

            }

            for(int i = 0; i < service_okay_list.size(); i++) {
                if(Test_host_list.contains(service_okay_list.get(i)[3])) {
                    Group_flag_table.put("Test_host_list", "green");
                }
                else if(Auth_host_list.contains(service_okay_list.get(i)[3])&& service_okay_list.get(i)[0].toUpperCase().contains("AUTHZ")){
                    Individual_Auth_table.put(service_okay_list.get(i)[3], "green");
                    Group_flag_table.put("Auth_host_list", "green");

                    if(DIT_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_DIT_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("DIT", "green");
                        Env_Auth_table.put("DIT", "green");
                    }
                    if(PERF_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PERF_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PERF", "green");
                        Env_Auth_table.put("PERF", "green");
                    }
                    if(Stage_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_Stage_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("Stage", "green");
                        Env_Auth_table.put("Stage", "green");
                    }
                    if(PROD_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PROD_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PROD", "green");
                        Env_Auth_table.put("PROD", "green");
                    }
                }
                else if(CA_host_list.contains(service_okay_list.get(i)[3])&& service_okay_list.get(i)[0].contains("CA")){
                    Individual_CA_table.put(service_okay_list.get(i)[3], "green");
                    Group_flag_table.put("CA_host_list", "green");

                    if(DIT_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_DIT_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("DIT", "green");
                        Env_CA_table.put("DIT", "green");
                    }
                    if(PERF_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PERF_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PERF", "green");
                        Env_CA_table.put("PERF", "green");
                    }
                    if(Stage_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_Stage_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("Stage", "green");
                        Env_CA_table.put("Stage", "green");
                    }
                    if(PROD_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PROD_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PROD", "green");
                        Env_CA_table.put("PROD", "green");
                    }
                }
                else if(EIS_host_list.contains(service_okay_list.get(i)[3])&& service_okay_list.get(i)[0].contains("EIS")){
                    Individual_EIS_table.put(service_okay_list.get(i)[3], "green");
                    Group_flag_table.put("EIS_host_list", "green");

                    if(DIT_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_DIT_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("DIT", "green");
                        Env_EIS_table.put("DIT", "green");
                    }
                    if(PERF_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PERF_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PERF", "green");
                        Env_EIS_table.put("PERF", "green");
                    }
                    if(Stage_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_Stage_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("Stage", "green");
                        Env_EIS_table.put("Stage", "green");
                    }
                    if(PROD_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PROD_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PROD", "green");
                        Env_EIS_table.put("PROD", "green");
                    }
                }
                else if(PIS_host_list.contains(service_okay_list.get(i)[3])&& service_okay_list.get(i)[0].contains("PIS")){
                    Individual_PIS_table.put(service_okay_list.get(i)[3], "green");
                    Group_flag_table.put("PIS_host_list", "green");

                    if(DIT_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_DIT_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("DIT", "green");
                        Env_PIS_table.put("DIT", "green");
                    }
                    if(PERF_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PERF_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PERF", "green");
                        Env_PIS_table.put("PERF", "green");
                    }
                    if(Stage_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_Stage_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("Stage", "green");
                        Env_PIS_table.put("Stage", "green");
                    }
                    if(PROD_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PROD_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PROD", "green");
                        Env_PIS_table.put("PROD", "green");
                    }
                }
                else if(IS_host_list.contains(service_okay_list.get(i)[3])&& service_okay_list.get(i)[0].contains("IS")){
                    Individual_IS_table.put(service_okay_list.get(i)[3], "green");
                    Group_flag_table.put("IS_host_list" ,"green");

                    if(DIT_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_DIT_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("DIT", "green");
                        Env_IS_table.put("DIT", "green");
                    }
                    if(PERF_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PERF_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PERF", "green");
                        Env_IS_table.put("PERF", "green");
                    }
                    if(Stage_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_Stage_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("Stage", "green");
                        Env_IS_table.put("Stage", "green");
                    }
                    if(PROD_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PROD_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PROD", "green");
                        Env_IS_table.put("PROD", "green");
                    }
                }
                else if(Polling_host_list.contains(service_okay_list.get(i)[3])&& service_okay_list.get(i)[0].contains("Polling")){
                    Individual_Polling_table.put(service_okay_list.get(i)[3], "green");
                    Group_flag_table.put("Polling_host_list", "green");

                    if(DIT_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_DIT_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("DIT", "green");
                        Env_Polling_table.put("DIT", "green");
                    }
                    if(PERF_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PERF_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PERF", "green");
                        Env_Polling_table.put("PERF", "green");
                    }
                    if(Stage_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_Stage_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("Stage", "green");
                        Env_Polling_table.put("Stage", "green");
                    }
                    if(PROD_host_list.contains(service_okay_list.get(i)[3])){
                        Individual_PROD_table.put(service_okay_list.get(i)[3], "green");
                        Env_group_table.put("PROD", "green");
                        Env_Polling_table.put("PROD", "green");
                    }
                }

                //System.out.println("Okay: " + Arrays.toString(service_okay_list.get(i)));
            }
            for (int i = 0; i < service_warning_list.size(); i++) {
                if(Test_host_list.contains(service_warning_list.get(i)[3])) {
                    Group_flag_table.put("Test_host_list", "yellow");
                }
                else if(Auth_host_list.contains(service_warning_list.get(i)[3]) && service_warning_list.get(i)[0].toUpperCase().contains("AUTHZ")){
                    Individual_Auth_table.put(service_warning_list.get(i)[3], "yellow");
                    Group_flag_table.put("Auth_host_list", "yellow");

                    if(DIT_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_DIT_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("DIT", "yellow");
                        Env_Auth_table.put("DIT", "yellow");
                    }
                    if(PERF_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PERF_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PERF", "yellow");
                        Env_Auth_table.put("PERF", "yellow");
                    }
                    if(Stage_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_Stage_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("Stage", "yellow");
                        Env_Auth_table.put("Stage", "yellow");
                    }
                    if(PROD_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PROD_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PROD", "yellow");
                        Env_Auth_table.put("PROD", "yellow");
                    }
                }
                else if(CA_host_list.contains(service_warning_list.get(i)[3]) && service_warning_list.get(i)[0].contains("CA")){
                    Individual_CA_table.put(service_warning_list.get(i)[3], "yellow");
                    Group_flag_table.put("CA_host_list", "yellow");

                    if(DIT_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_DIT_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("DIT", "yellow");
                        Env_CA_table.put("DIT", "yellow");
                    }
                    if(PERF_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PERF_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PERF", "yellow");
                        Env_CA_table.put("PERF", "yellow");
                    }
                    if(Stage_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_Stage_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("Stage", "yellow");
                        Env_CA_table.put("Stage", "yellow");
                    }
                    if(PROD_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PROD_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PROD", "yellow");
                        Env_CA_table.put("PROD", "yellow");
                    }
                }
                else if(EIS_host_list.contains(service_warning_list.get(i)[3]) && service_warning_list.get(i)[0].contains("EIS")){
                    Individual_EIS_table.put(service_warning_list.get(i)[3], "yellow");
                    Group_flag_table.put("EIS_host_list", "yellow");

                    if(DIT_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_DIT_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("DIT", "yellow");
                        Env_EIS_table.put("DIT", "yellow");
                    }
                    if(PERF_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PERF_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PERF", "yellow");
                        Env_EIS_table.put("PERF", "yellow");
                    }
                    if(Stage_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_Stage_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("Stage", "yellow");
                        Env_EIS_table.put("Stage", "yellow");
                    }
                    if(PROD_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PROD_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PROD", "yellow");
                        Env_EIS_table.put("PROD", "yellow");
                    }
                }
                else if(PIS_host_list.contains(service_warning_list.get(i)[4]) && service_warning_list.get(i)[0].contains("PIS")){
                    Individual_PIS_table.put(service_warning_list.get(i)[4], "yellow");
                    Group_flag_table.put("PIS_host_list", "yellow");

                    if(DIT_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_DIT_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("DIT", "yellow");
                        Env_PIS_table.put("DIT", "yellow");
                    }
                    if(PERF_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PERF_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PERF", "yellow");
                        Env_PIS_table.put("PERF", "yellow");
                    }
                    if(Stage_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_Stage_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("Stage", "yellow");
                        Env_PIS_table.put("Stage", "yellow");
                    }
                    if(PROD_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PROD_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PROD", "yellow");
                        Env_PIS_table.put("PROD", "yellow");
                    }
                }
                else if(IS_host_list.contains(service_warning_list.get(i)[3]) && service_warning_list.get(i)[0].contains("IS")){
                    Individual_IS_table.put(service_warning_list.get(i)[3], "yellow");
                    Group_flag_table.put("IS_host_list" ,"yellow");

                    if(DIT_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_DIT_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("DIT", "yellow");
                        Env_IS_table.put("DIT", "yellow");
                    }
                    if(PERF_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PERF_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PERF", "yellow");
                        Env_IS_table.put("PERF", "yellow");
                    }
                    if(Stage_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_Stage_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("Stage", "yellow");
                        Env_IS_table.put("Stage", "yellow");
                    }
                    if(PROD_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PROD_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PROD", "yellow");
                        Env_IS_table.put("PROD", "yellow");
                    }
                }
                else if(Polling_host_list.contains(service_warning_list.get(i)[3]) && service_warning_list.get(i)[0].contains("Polling")){
                    Individual_Polling_table.put(service_warning_list.get(i)[3], "yellow");
                    Group_flag_table.put("Polling_host_list", "yellow");

                    if(DIT_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_DIT_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("DIT", "yellow");
                        Env_Polling_table.put("DIT", "yellow");
                    }
                    if(PERF_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PERF_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PERF", "yellow");
                        Env_Polling_table.put("PERF", "yellow");
                    }
                    if(Stage_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_Stage_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("Stage", "yellow");
                        Env_Polling_table.put("Stage", "yellow");
                    }
                    if(PROD_host_list.contains(service_warning_list.get(i)[3])){
                        Individual_PROD_table.put(service_warning_list.get(i)[3], "yellow");
                        Env_group_table.put("PROD", "yellow");
                        Env_Polling_table.put("PROD", "yellow");
                    }
                }

                System.out.println("Warning: " + Arrays.toString(service_warning_list.get(i)));
            }

            for (int i = 0; i < service_critical_list.size(); i++) {

                if(Test_host_list.contains(service_critical_list.get(i)[3])) {
                    Group_flag_table.put("Test_host_list", "red");
                }
                else if(Auth_host_list.contains(service_critical_list.get(i)[3]) && service_critical_list.get(i)[0].toUpperCase().contains("AUTHZ")){
                    Individual_Auth_table.put(service_critical_list.get(i)[3], "red");
                    Group_flag_table.put("Auth_host_list", "red");

                    if(DIT_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_DIT_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("DIT", "red");
                        Env_Auth_table.put("DIT", "red");
                    }
                    if(PERF_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PERF_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PERF", "red");
                        Env_Auth_table.put("PERF", "red");
                    }
                    if(Stage_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_Stage_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("Stage", "red");
                        Env_Auth_table.put("Stage", "red");
                    }
                    if(PROD_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PROD_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PROD", "red");
                        Env_Auth_table.put("PROD", "red");
                    }
                }
                else if(CA_host_list.contains(service_critical_list.get(i)[3]) && service_critical_list.get(i)[0].contains("CA")){
                    Individual_CA_table.put(service_critical_list.get(i)[3], "red");
                    Group_flag_table.put("CA_host_list", "red");

                    if(DIT_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_DIT_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("DIT", "red");
                        Env_CA_table.put("DIT", "red");
                    }
                    if(PERF_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PERF_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PERF", "red");
                        Env_CA_table.put("PERF", "red");
                    }
                    if(Stage_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_Stage_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("Stage", "red");
                        Env_CA_table.put("Stage", "red");
                    }
                    if(PROD_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PROD_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PROD", "red");
                        Env_CA_table.put("PROD", "red");
                    }
                }
                else if(EIS_host_list.contains(service_critical_list.get(i)[3]) && service_critical_list.get(i)[0].contains("EIS")){
                    Individual_EIS_table.put(service_critical_list.get(i)[3], "red");
                    Group_flag_table.put("EIS_host_list", "red");

                    if(DIT_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_DIT_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("DIT", "red");
                        Env_EIS_table.put("DIT", "red");
                    }
                    if(PERF_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PERF_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PERF", "red");
                        Env_EIS_table.put("PERF", "red");
                    }
                    if(Stage_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_Stage_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("Stage", "red");
                        Env_EIS_table.put("Stage", "red");
                    }
                    if(PROD_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PROD_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PROD", "red");
                        Env_EIS_table.put("PROD", "red");
                    }
                }
                else if(PIS_host_list.contains(service_critical_list.get(i)[3]) && service_critical_list.get(i)[0].contains("PIS")){
                    Individual_PIS_table.put(service_critical_list.get(i)[3], "red");
                    Group_flag_table.put("PIS_host_list", "red");

                    if(DIT_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_DIT_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("DIT", "red");
                        Env_PIS_table.put("DIT", "red");
                    }
                    if(PERF_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PERF_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PERF", "red");
                        Env_PIS_table.put("PERF", "red");
                    }
                    if(Stage_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_Stage_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("Stage", "red");
                        Env_PIS_table.put("Stage", "red");
                    }
                    if(PROD_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PROD_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PROD", "red");
                        Env_PIS_table.put("PROD", "red");
                    }
                }
                else if(IS_host_list.contains(service_critical_list.get(i)[3]) && service_critical_list.get(i)[0].contains("IS")){
                    Individual_IS_table.put(service_critical_list.get(i)[3], "red");
                    Group_flag_table.put("IS_host_list" ,"red");

                    if(DIT_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_DIT_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("DIT", "red");
                        Env_IS_table.put("DIT", "red");
                    }
                    if(PERF_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PERF_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PERF", "red");
                        Env_IS_table.put("PERF", "red");
                    }
                    if(Stage_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_Stage_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("Stage", "red");
                        Env_IS_table.put("Stage", "red");
                    }
                    if(PROD_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PROD_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PROD", "red");
                        Env_IS_table.put("PROD", "red");
                    }
                }
                else if(Polling_host_list.contains(service_critical_list.get(i)[3]) && service_critical_list.get(i)[0].contains("Polling")) {
                    Individual_Polling_table.put(service_critical_list.get(i)[3], "red");
                    Group_flag_table.put("Polling_host_list", "red");

                    if(DIT_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_DIT_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("DIT", "red");
                        Env_Polling_table.put("DIT", "red");
                    }
                    if(PERF_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PERF_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PERF", "red");
                        Env_Polling_table.put("PERF", "red");
                    }
                    if(Stage_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_Stage_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("Stage", "red");
                        Env_Polling_table.put("Stage", "red");
                    }
                    if(PROD_host_list.contains(service_critical_list.get(i)[3])){
                        Individual_PROD_table.put(service_critical_list.get(i)[3], "red");
                        Env_group_table.put("PROD", "red");
                        Env_Polling_table.put("PROD", "red");
                    }
                }
                System.out.println("Critical: " + Arrays.toString(service_critical_list.get(i)));
            }

            /*System.out.println("Service okay total: " + service_okay_list.size());
            System.out.println("Service critical total: " + service_critical_list.size());
            System.out.println("Service warning total: " + service_warning_list.size());
            System.out.println("Group table: " + Group_flag_table);
            System.out.println("Individual table: " + Individual_flag_table + " " + Individual_flag_table.size());
            System.out.println("Individual Auth table: " + Individual_Auth_table);
            System.out.println("Individual Auth table size: " + Individual_Auth_table.size());
            System.out.println("Individual CA table: " + Individual_CA_table);
            System.out.println("Individual CA table size: " + Individual_CA_table.size());
            System.out.println("Individual EIS table: " + Individual_EIS_table);
            System.out.println("Individual EIS table size: " + Individual_EIS_table.size());
            System.out.println("Individual IS table: " + Individual_IS_table);
            System.out.println("Individual IS table size: " + Individual_IS_table.size());
            System.out.println("Individual PIS table: " + Individual_PIS_table);
            System.out.println("Individual PIS table size: " + Individual_PIS_table.size());
            System.out.println("Individual Polling table: " + Individual_Polling_table);
            System.out.println("Individual Polling table size: " + Individual_Polling_table.size());*/

            System.out.println("Flag: " + clear_flag);

            if(clear_flag == true){

                final ArrayList<JSONObject> T_list;

                ArrayList<JSONObject> Group_list = new ArrayList<>();
                ArrayList<JSONObject> Auth_list = new ArrayList<>();
                ArrayList<JSONObject> CA_list = new ArrayList<>();
                ArrayList<JSONObject> EIS_list = new ArrayList<>();
                ArrayList<JSONObject> IS_list = new ArrayList<>();
                ArrayList<JSONObject> PIS_list = new ArrayList<>();
                ArrayList<JSONObject> Polling_list = new ArrayList<>();
                ArrayList<JSONObject> Master_list = new ArrayList<>();
                ArrayList<JSONObject> Env_list = new ArrayList<>();
                ArrayList<JSONObject> DIT_list = new ArrayList<>();
                ArrayList<JSONObject> DIT_detail_list = new ArrayList<>();
                ArrayList<JSONObject> PERF_list = new ArrayList<>();
                ArrayList<JSONObject> PERF_detail_list = new ArrayList<>();
                ArrayList<JSONObject> Stage_list = new ArrayList<>();
                ArrayList<JSONObject> Stage_detail_list = new ArrayList<>();
                ArrayList<JSONObject> PROD_list = new ArrayList<>();
                ArrayList<JSONObject> PROD_detail_list = new ArrayList<>();
                T_list = Test_list;

                Enumeration<String> e1 = Group_flag_table.keys();
                while (e1.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e1.nextElement();
                    t.put("name", key);
                    t.put("color", Group_flag_table.get(key));
                    Group_list.add(t);

                }

                Enumeration<String> e2 = Individual_Auth_table.keys();
                while (e2.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e2.nextElement();
                    t.put("name", key);
                    t.put("color", Individual_Auth_table.get(key));
                    Auth_list.add(t);

                }

                Enumeration<String> e3 = Individual_CA_table.keys();
                while (e3.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e3.nextElement();
                    t.put("name", key);
                    t.put("color", Individual_CA_table.get(key));
                    CA_list.add(t);

                }

                Enumeration<String> e4 = Individual_EIS_table.keys();
                while (e4.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e4.nextElement();
                    t.put("name", key);
                    t.put("color", Individual_EIS_table.get(key));
                    EIS_list.add(t);

                }

                Enumeration<String> e5 = Individual_IS_table.keys();
                while (e5.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e5.nextElement();
                    t.put("name", key);
                    t.put("color", Individual_IS_table.get(key));
                    IS_list.add(t);

                }

                Enumeration<String> e6 = Individual_PIS_table.keys();
                while (e6.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e6.nextElement();
                    t.put("name", key);
                    t.put("color", Individual_PIS_table.get(key));
                    PIS_list.add(t);

                }

                Enumeration<String> e7 = Individual_Polling_table.keys();
                while (e7.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e7.nextElement();
                    t.put("name", key);
                    t.put("color", Individual_Polling_table.get(key));
                    Polling_list.add(t);

                }

                Enumeration<String> e8 = Individual_DIT_table.keys();
                while (e8.hasMoreElements()) {
                    String key = e8.nextElement();
                    if(Auth_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Authorization Service");
                        t.put("name", key);
                        t.put("color", Individual_DIT_table.get(key));
                        DIT_list.add(t);
                    }
                    if(CA_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Common Admin");
                        t.put("name", key);
                        t.put("color", Individual_DIT_table.get(key));
                        DIT_list.add(t);
                    }
                    if(EIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Enterprise Service");
                        t.put("name", key);
                        t.put("color", Individual_DIT_table.get(key));
                        DIT_list.add(t);
                    }
                    if(IS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Identity Service");
                        t.put("name", key);
                        t.put("color", Individual_DIT_table.get(key));
                        DIT_list.add(t);
                    }
                    if(PIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Product Service");
                        t.put("name", key);
                        t.put("color", Individual_DIT_table.get(key));
                        DIT_list.add(t);
                    }
                    if(Polling_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Polling Service");
                        t.put("name", key);
                        t.put("color", Individual_DIT_table.get(key));
                        DIT_list.add(t);
                    }

                    //DIT_list.add(t);
                }
                Enumeration<String> e9 = Individual_PERF_table.keys();
                while (e9.hasMoreElements()) {
                    String key = e9.nextElement();
                    if(Auth_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Authorization Service");
                        t.put("name", key);
                        t.put("color", Individual_PERF_table.get(key));
                        PERF_list.add(t);
                    }
                    if(CA_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Common Admin");
                        t.put("name", key);
                        t.put("color", Individual_PERF_table.get(key));
                        PERF_list.add(t);
                    }
                    if(EIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Enterprise Service");
                        t.put("name", key);
                        t.put("color", Individual_PERF_table.get(key));
                        PERF_list.add(t);
                    }
                    if(IS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Identity Service");
                        t.put("name", key);
                        t.put("color", Individual_PERF_table.get(key));
                        PERF_list.add(t);
                    }
                    if(PIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Product Service");
                        t.put("name", key);
                        t.put("color", Individual_PERF_table.get(key));
                        PERF_list.add(t);
                    }
                    if(Polling_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Polling Service");
                        t.put("name", key);
                        t.put("color", Individual_PERF_table.get(key));
                        PERF_list.add(t);
                    }

                    //PERF_list.add(t);

                }
                Enumeration<String> e10 = Individual_Stage_table.keys();
                while (e10.hasMoreElements()) {
                    String key = e10.nextElement();
                    if(Auth_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Authorization Service");
                        t.put("name", key);
                        t.put("color", Individual_Stage_table.get(key));
                        Stage_list.add(t);
                    }
                    if(CA_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Common Admin");
                        t.put("name", key);
                        t.put("color", Individual_Stage_table.get(key));
                        Stage_list.add(t);
                    }
                    if(EIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Enterprise Service");
                        t.put("name", key);
                        t.put("color", Individual_Stage_table.get(key));
                        Stage_list.add(t);
                    }
                    if(IS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Identity Service");
                        t.put("name", key);
                        t.put("color", Individual_Stage_table.get(key));
                        Stage_list.add(t);
                    }
                    if(PIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Product Service");
                        t.put("name", key);
                        t.put("color", Individual_Stage_table.get(key));
                        Stage_list.add(t);
                    }
                    if(Polling_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Polling Service");
                        t.put("name", key);
                        t.put("color", Individual_Stage_table.get(key));
                        Stage_list.add(t);
                    }

                    //Stage_list.add(t);

                }
                Enumeration<String> e11 = Individual_PROD_table.keys();
                while (e11.hasMoreElements()) {
                    String key = e11.nextElement();
                    if(Auth_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Authorization Service");
                        t.put("name", key);
                        t.put("color", Individual_PROD_table.get(key));
                        PROD_list.add(t);
                    }
                    if(CA_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Common Admin");
                        t.put("name", key);
                        t.put("color", Individual_PROD_table.get(key));
                        PROD_list.add(t);
                    }
                    if(EIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Enterprise Service");
                        t.put("name", key);
                        t.put("color", Individual_PROD_table.get(key));
                        PROD_list.add(t);
                    }
                    if(IS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Identity Service");
                        t.put("name", key);
                        t.put("color", Individual_PROD_table.get(key));
                        PROD_list.add(t);
                    }
                    if(PIS_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Product Service");
                        t.put("name", key);
                        t.put("color", Individual_PROD_table.get(key));
                        PROD_list.add(t);
                    }
                    if(Polling_host_list.contains(key)){
                        JSONObject t = new JSONObject();
                        t.put("group", "Polling Service");
                        t.put("name", key);
                        t.put("color", Individual_PROD_table.get(key));
                        PROD_list.add(t);
                    }

                    //PROD_list.add(t);

                }
                Enumeration<String> e12 = Env_group_table.keys();
                while (e12.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e12.nextElement();
                    t.put("name", key);
                    t.put("color", Env_group_table.get(key));
                    Env_list.add(t);

                }
                Enumeration<String> e13 = Env_Auth_table.keys();
                while (e13.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e13.nextElement();
                    if(key.equals("DIT")){
                        t.put("name", "Authorization Service");
                        t.put("color", Env_Auth_table.get(key));
                        DIT_detail_list.add(t);
                    }
                    else if(key.equals("PERF")){
                        t.put("name", "Authorization Service");
                        t.put("color", Env_Auth_table.get(key));
                        PERF_detail_list.add(t);
                    }
                    else if(key.equals("Stage")){
                        t.put("name", "Authorization Service");
                        t.put("color", Env_Auth_table.get(key));
                        Stage_detail_list.add(t);
                    }
                    else if(key.equals("PROD")){
                        t.put("name", "Authorization Service");
                        t.put("color", Env_Auth_table.get(key));
                        PROD_detail_list.add(t);
                    }

                }
                Enumeration<String> e14 = Env_CA_table.keys();
                while (e14.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e14.nextElement();
                    if(key.equals("DIT")){
                        t.put("name", "Common Admin");
                        t.put("color", Env_CA_table.get(key));
                        DIT_detail_list.add(t);
                    }
                    else if(key.equals("PERF")){
                        t.put("name", "Common Admin");
                        t.put("color", Env_CA_table.get(key));
                        PERF_detail_list.add(t);
                    }
                    else if(key.equals("Stage")){
                        t.put("name", "Common Admin");
                        t.put("color", Env_CA_table.get(key));
                        Stage_detail_list.add(t);
                    }
                    else if(key.equals("PROD")){
                        t.put("name", "Common Admin");
                        t.put("color", Env_CA_table.get(key));
                        PROD_detail_list.add(t);
                    }

                }
                Enumeration<String> e15 = Env_EIS_table.keys();
                while (e15.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e15.nextElement();
                    if(key.equals("DIT")){
                        t.put("name", "Enterprise Service");
                        t.put("color", Env_EIS_table.get(key));
                        DIT_detail_list.add(t);
                    }
                    else if(key.equals("PERF")){
                        t.put("name", "Enterprise Service");
                        t.put("color", Env_EIS_table.get(key));
                        PERF_detail_list.add(t);
                    }
                    else if(key.equals("Stage")){
                        t.put("name", "Enterprise Service");
                        t.put("color", Env_EIS_table.get(key));
                        Stage_detail_list.add(t);
                    }
                    else if(key.equals("PROD")){
                        t.put("name", "Enterprise Service");
                        t.put("color", Env_EIS_table.get(key));
                        PROD_detail_list.add(t);
                    }

                }
                Enumeration<String> e16 = Env_IS_table.keys();
                while (e16.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e16.nextElement();
                    if(key.equals("DIT")){
                        t.put("name", "Identity Service");
                        t.put("color", Env_IS_table.get(key));
                        DIT_detail_list.add(t);
                    }
                    else if(key.equals("PERF")){
                        t.put("name", "Identity Service");
                        t.put("color", Env_IS_table.get(key));
                        PERF_detail_list.add(t);
                    }
                    else if(key.equals("Stage")){
                        t.put("name", "Identity Service");
                        t.put("color", Env_IS_table.get(key));
                        Stage_detail_list.add(t);
                    }
                    else if(key.equals("PROD")){
                        t.put("name", "Identity Service");
                        t.put("color", Env_IS_table.get(key));
                        PROD_detail_list.add(t);
                    }

                }
                Enumeration<String> e17 = Env_PIS_table.keys();
                while (e17.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e17.nextElement();
                    if(key.equals("DIT")){
                        t.put("name", "Product Service");
                        t.put("color", Env_PIS_table.get(key));
                        DIT_detail_list.add(t);
                    }
                    else if(key.equals("PERF")){
                        t.put("name", "Product Service");
                        t.put("color", Env_PIS_table.get(key));
                        PERF_detail_list.add(t);
                    }
                    else if(key.equals("Stage")){
                        t.put("name", "Product Service");
                        t.put("color", Env_PIS_table.get(key));
                        Stage_detail_list.add(t);
                    }
                    else if(key.equals("PROD")){
                        t.put("name", "Product Service");
                        t.put("color", Env_PIS_table.get(key));
                        PROD_detail_list.add(t);
                    }

                }
                Enumeration<String> e18 = Env_Polling_table.keys();
                while (e18.hasMoreElements()) {
                    JSONObject t = new JSONObject();
                    String key = e18.nextElement();
                    if(key.equals("DIT")){
                        t.put("name", "Polling Service");
                        t.put("color", Env_Polling_table.get(key));
                        DIT_detail_list.add(t);
                    }
                    else if(key.equals("PERF")){
                        t.put("name", "Polling Service");
                        t.put("color", Env_Polling_table.get(key));
                        PERF_detail_list.add(t);
                    }
                    else if(key.equals("Stage")){
                        t.put("name", "Polling Service");
                        t.put("color", Env_Polling_table.get(key));
                        Stage_detail_list.add(t);
                    }
                    else if(key.equals("PROD")){
                        t.put("name", "Polling Service");
                        t.put("color", Env_Polling_table.get(key));
                        PROD_detail_list.add(t);
                    }

                }
                //System.out.println("TESTING: " + Group_list);

                obj7.put("Env_list", Env_list);
                obj7.put("DIT_list", DIT_list);
                obj7.put("PERF_list", PERF_list);
                obj7.put("Stage_list", Stage_list);
                obj7.put("PROD_list", PROD_list);
                obj7.put("Group_list", Group_list);
                obj7.put("Authorization Service", Auth_list);
                obj7.put("Common Admin", CA_list);
                obj7.put("Enterprise Service", EIS_list);
                obj7.put("Identity Service", IS_list);
                obj7.put("Product Service", PIS_list);
                obj7.put("Polling Service", Polling_list);
                obj7.put("Details", T_list);
                obj7.put("DIT", DIT_detail_list);
                obj7.put("PERF", PERF_detail_list);
                obj7.put("Stage", Stage_detail_list);
                obj7.put("PROD", PROD_detail_list);
                Master_list.add(obj7);
                obj8.put("Individual_lists", Master_list);

                //System.out.println(obj7);
                //System.out.println(obj8);
                System.out.println("DETAILS!!!!!: " + Test_list);
                System.out.println("SIZE!!!!!: " + Test_list.size());

                DIT_host_list.clear();
                PERF_host_list.clear();
                Stage_host_list.clear();
                PROD_host_list.clear();
                Auth_host_list.clear();
                CA_host_list.clear();
                EIS_host_list.clear();
                IS_host_list.clear();
                PIS_host_list.clear();
                Polling_host_list.clear();
                Test_host_list.clear();
                service_okay_list.clear();
                service_warning_list.clear();
                service_critical_list.clear();
                //Test_list.clear();



                clear_flag = false;
            }
            else {
                clear_flag = true;

            }

            System.out.println("Flag: " + clear_flag);


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Returned: "+ obj8);
        System.out.println("Returned size: " + obj8.size());
        System.out.println("DETAILS Size!!!!!: " + Test_list.size());
        //Test_list.clear();
        System.out.println("Returned2: "+ obj8);
        System.out.println("Returned2 size: " + obj8.size());
        return obj8;


    }



    private static void print_lists() {
        System.out.println("Final List: " + Final_host_list);
        System.out.println("Final List: " + Final_host_list.size());
        System.out.println("New List: " + New_host_list);
        System.out.println("New List: " + New_host_list.size());
        System.out.println("Auth List: " + Auth_host_list);
        System.out.println("Auth List: " + Auth_host_list.size());
        System.out.println("CA List: " + CA_host_list);
        System.out.println("CA List: " + CA_host_list.size());
        System.out.println("EIS List: " + EIS_host_list);
        System.out.println("EIS List: " + EIS_host_list.size());
        System.out.println("IS List: " + IS_host_list);
        System.out.println("IS List: " + IS_host_list.size());
        System.out.println("PIS List: " + PIS_host_list);
        System.out.println("PIS List: " + PIS_host_list.size());
        System.out.println("Polling List: " + Polling_host_list);
        System.out.println("Polling List: " + Polling_host_list.size());
    }

    private static String getUrl(String[] object){
        if(object[8].startsWith("check_xi_service_http") && !object[3].startsWith("c")){
            int start = object[8].indexOf(";/");
            //if(object[8].substring(start - 1, start).equals(";")){
            int from = start + 1;
            int to = object[8].indexOf('&', from+1);
            object[8] = object[8].substring(from, to);
            if(object[8].contains("AuthorizationService")){ //The original url in the xml feeds leads to nothing. This modifies it to display the actual health check
                object[8] = "/AuthorizationService/health";
            }
            else if(object[8].contains("commonproductservice")){ //The original url in the xml feeds leads to nothing. This modifies it to display the actual health check
                object[8] = "/commonproductservice/health";
            }
            //t.put("url", object[3] + object[8]);
            //t.put("check_command", object[8]);
            //}

            //t.put("check_command", object[8]);
            return object[3]+object[8];

        }
        else if(object[8].startsWith("check_xi_service_http") && object[3].startsWith("c")){
            int portStart = object[8].indexOf("-p");
            int portEnd = portStart + 7;
            //int portEnd = object[8].indexOf("-", portStart+1);
            object[9] = object[8].substring(portStart + 3, portEnd);
            int start = object[8].indexOf(";/");
            int from = start + 1;
            int to = object[8].indexOf('&', from+1);
            object[8] = object[8].substring(from, to);
            //t.put("url", object[3] + object[8]);
            //t.put("url", object[3]+":"+object[9]+object[8]);
            return object[3]+":"+object[9]+object[8];

        }
        else if(object[8].startsWith("check_http_!") && object[3].startsWith("c")){
            int portStart = object[8].indexOf("-p");
            int portEnd = portStart + 7;
            //int portEnd = object[8].indexOf("-", portStart+1);
            object[9] = object[8].substring(portStart + 3, portEnd);
            int start = object[8].indexOf(";/");
            int from = start + 1;
            int to = object[8].indexOf(';', from+1);
            object[8] = object[8].substring(from, to);
            //t.put("url", object[3] + object[8]);
            //t.put("url", object[3]+":"+object[9]+object[8]);
            return object[3]+":"+object[9]+object[8];

        }
        else{
            return null;
        }

    }


}
