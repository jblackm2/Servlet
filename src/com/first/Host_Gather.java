package com.first;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by blackmju on 7/23/2015.
 */
public class Host_Gather {

    static ArrayList<String> DIT_host_list = new ArrayList<>();  //Tracks the list of hosts in DIT
    static ArrayList<String> PERF_host_list = new ArrayList<>();  //Tracks the list of hosts in PERF
    static ArrayList<String> Stage_host_list = new ArrayList<>();  //Tracks the list of hosts in Stage
    static ArrayList<String> PROD_host_list = new ArrayList<>();  //Tracks the list of hosts in PROD
    static ArrayList<String> Auth_host_list = new ArrayList<>();  //Tracks the list of hosts in the Auth team
    static ArrayList<String> CA_host_list = new ArrayList<>();  //Tracks the list of hosts in the CA team
    static ArrayList<String> EIS_host_list = new ArrayList<>();  //Tracks the list of hosts in the EIS team
    static ArrayList<String> IS_host_list = new ArrayList<>();  //Tracks the list of hosts in the IS team
    static ArrayList<String> PIS_host_list = new ArrayList<>();  //Tracks the list of hosts in the PIS team
    static ArrayList<String> Polling_host_list = new ArrayList<>();   //Tracks the list of hosts in the Polling team
    static ArrayList<String> Test_host_list = new ArrayList<>();  //Tracks the list of hosts if I have any test hosts
    static ArrayList<String> Temp_host_list = new ArrayList<>(); //Used to check if a host name has been added to the Final_host_list yet
    static Hashtable<String, String> Env_group_table = new Hashtable<>();  //Tracks the status of each environment
    static Hashtable<String, String> Env_Auth_table = new Hashtable<>();  //Tracks the status of the Auth team in each environment
    static Hashtable<String, String> Env_CA_table = new Hashtable<>();  //Tracks the status of the CA team in each environment
    static Hashtable<String, String> Env_EIS_table = new Hashtable<>();  //Tracks the status of the EIS team in each environment
    static Hashtable<String, String> Env_IS_table = new Hashtable<>();  //Tracks the status of the IS team in each environment
    static Hashtable<String, String> Env_PIS_table = new Hashtable<>();  //Tracks the status of the PIS team in each environment
    static Hashtable<String, String> Env_Polling_table = new Hashtable<>();  //Tracks the status of the Polling team in each environment
    static Hashtable<String, String> Group_flag_table = new Hashtable<>();  //Tracks the status of each team for each environment
    static Hashtable<String, String> Individual_DIT_table = new Hashtable<>();  //Tracks the status of the individual host in the DIT environment
    static Hashtable<String, String> Individual_PERF_table = new Hashtable<>();  //Tracks the status of the individual host in the PERF environment
    static Hashtable<String, String> Individual_Stage_table = new Hashtable<>();  //Tracks the status of the individual host in the Stage environment
    static Hashtable<String, String> Individual_PROD_table = new Hashtable<>();  //Tracks the status of the individual host in the PROD environment
    static Hashtable<String, String> Individual_flag_table = new Hashtable<>();  //Tracks the status of each individual host
    static Hashtable<String, String> Individual_Auth_table = new Hashtable<>();  //Tracks the status of each host in the Auth team
    static Hashtable<String, String> Individual_CA_table = new Hashtable<>();  //Tracks the status of each host in the CA team
    static Hashtable<String, String> Individual_EIS_table = new Hashtable<>();  //Tracks the status of each host in the EIS team
    static Hashtable<String, String> Individual_IS_table = new Hashtable<>();  //Tracks the status of each host in the IS team
    static Hashtable<String, String> Individual_PIS_table = new Hashtable<>();  //Tracks the status of each host in the PIS team
    static Hashtable<String, String> Individual_Polling_table = new Hashtable<>();  //Tracks the status of each host in the Polling team
    static List<String[]> host_okay_list = new ArrayList<String[]>(); //List of hosts with the okay status
    static List<String[]> host_warning_list = new ArrayList<String[]>();  //List of hosts with the warning status
    static List<String[]> host_critical_list = new ArrayList<String[]>();  //List of hosts with the critical status
    static ArrayList<String> Final_host_list = new ArrayList<>();  //The final list of all acceptable host names
    static Boolean clear_flag = false;  //****Very important, is triggered once the data from the PROD Url is returned. This means that I have all the data from both Url feeds.
                                                // Used to determine if the host lists should be cleared and if Json Object should be returned.

    //static ArrayList<JSONObject> Test_list = new ArrayList<>();


    public static JSONObject gather(String Host_url, ArrayList<String> host_list) {//Need to implement the executor shutdown

        System.out.println(Thread.currentThread().getName() + " Host task");

        String [] temp = null;
        JSONObject obj7 = new JSONObject();
        JSONObject obj8 = new JSONObject();



        ArrayList<JSONObject> Env_list = new ArrayList<>();
        ArrayList<JSONObject> DIT_list = new ArrayList<>();
        ArrayList<JSONObject> DIT_detail_list = new ArrayList<>();
        ArrayList<JSONObject> PERF_list = new ArrayList<>();
        ArrayList<JSONObject> PERF_detail_list = new ArrayList<>();
        ArrayList<JSONObject> Stage_list = new ArrayList<>();
        ArrayList<JSONObject> Stage_detail_list = new ArrayList<>();
        ArrayList<JSONObject> PROD_list = new ArrayList<>();
        ArrayList<JSONObject> PROD_detail_list = new ArrayList<>();
        ArrayList<JSONObject> Group_list = new ArrayList<>();
        ArrayList<JSONObject> Auth_list = new ArrayList<>();
        ArrayList<JSONObject> CA_list = new ArrayList<>();
        ArrayList<JSONObject> EIS_list = new ArrayList<>();
        ArrayList<JSONObject> IS_list = new ArrayList<>();
        ArrayList<JSONObject> PIS_list = new ArrayList<>();
        ArrayList<JSONObject> Polling_list = new ArrayList<>();
        ArrayList<JSONObject> Master_list = new ArrayList<>();

        for(int i = 0; i< host_list.size(); i++){
            temp = host_list.get(i).split("\\s*:\\s*");
            //System.out.println(temp[0] + temp[1]);
            if(!Temp_host_list.contains(temp[2])){
                Final_host_list.add(temp[2]);
                Temp_host_list.add(temp[1]);
                Temp_host_list.add(temp[2]);
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
            if (temp[1].equals("Auth")&& !Auth_host_list.contains(temp[2])){
                Auth_host_list.add(temp[2]);
            }
            else if(temp[1].equals("CA")&& !CA_host_list.contains(temp[2])){
                CA_host_list.add(temp[2]);
            }
            else if(temp[1].equals("EIS")&& !EIS_host_list.contains(temp[2])){
                EIS_host_list.add(temp[2]);
            }
            else if(temp[1].equals("IS")&& !IS_host_list.contains(temp[2])){
                IS_host_list.add(temp[2]);
            }
            else if(temp[1].equals("PIS")&& !PIS_host_list.contains(temp[2])){
                PIS_host_list.add(temp[2]);
            }
            else if(temp[1].equals("Polling")&& !Polling_host_list.contains(temp[2])){
                Polling_host_list.add(temp[2]);
            }
            else if(temp[1].contains("Test")&& !Test_host_list.contains(temp[2])){
                Test_host_list.add(temp[2]);
            }
            else{
                //System.out.println("Not a valid host_list");
            }

        }

        //print_lists();


        URL url = null;

        try {
            url = new URL(Host_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = builder.parse(conn.getInputStream());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        String name = doc.getDocumentElement().getNodeName();
        System.out.println(name);

        NodeList list = doc.getElementsByTagName("hoststatus");

        for (int i = 0; i < list.getLength(); i++) {

            Node node = list.item(i);
            //System.out.println("Node: " + node.getNodeName());
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String host_name = element.getElementsByTagName("name").item(0).getTextContent();
                if(Final_host_list.contains(host_name)){
                    String a = element.getElementsByTagName("current_state").item(0).getTextContent();
                    if (a.equals("0")) {
                        String[] object = new String[5];
                        object[0] = (element.getElementsByTagName("host_id").item(0).getTextContent());
                        object[1] = (element.getElementsByTagName("status_text").item(0).getTextContent());
                        object[2] = (element.getElementsByTagName("status_update_time").item(0).getTextContent());
                        object[3] = (element.getElementsByTagName("name").item(0).getTextContent());
                        object[4] = (element.getElementsByTagName("current_state").item(0).getTextContent());
                        host_okay_list.add(object);
                        Individual_flag_table.put(object[3], "green");
                    }
                    else if (a.equals("1")) {
                        String[] object = new String[5];
                        object[0] = (element.getElementsByTagName("host_id").item(0).getTextContent());
                        object[1] = (element.getElementsByTagName("status_text").item(0).getTextContent());
                        object[2] = (element.getElementsByTagName("status_update_time").item(0).getTextContent());
                        object[3] = (element.getElementsByTagName("name").item(0).getTextContent());
                        object[4] = (element.getElementsByTagName("current_state").item(0).getTextContent());
                        host_warning_list.add(object);
                        Individual_flag_table.put(object[3], "yellow");
                    }
                    else if (a.equals("2")) {
                        String[] object = new String[5];
                        object[0] = (element.getElementsByTagName("host_id").item(0).getTextContent());
                        object[1] = (element.getElementsByTagName("status_text").item(0).getTextContent());
                        object[2] = (element.getElementsByTagName("status_update_time").item(0).getTextContent());
                        object[3] = (element.getElementsByTagName("name").item(0).getTextContent());
                        object[4] = (element.getElementsByTagName("current_state").item(0).getTextContent());
                        host_critical_list.add(object);
                        Individual_flag_table.put(object[3], "red");
                    }
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

        for (int i = 0; i < host_okay_list.size(); i++) {

            if(Test_host_list.contains(host_okay_list.get(i)[3])) {
                Group_flag_table.put("Test_host_list", "green");
            }
            if(Auth_host_list.contains(host_okay_list.get(i)[3])){
                Individual_Auth_table.put(host_okay_list.get(i)[3], "green");
                Group_flag_table.put("Auth_host_list", "green");

                if(DIT_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_DIT_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("DIT", "green");
                    Env_Auth_table.put("DIT", "green");
                }
                if(PERF_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PERF_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PERF", "green");
                    Env_Auth_table.put("PERF", "green");
                }
                if(Stage_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_Stage_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("Stage", "green");
                    Env_Auth_table.put("Stage", "green");
                }
                if(PROD_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PROD_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PROD", "green");
                    Env_Auth_table.put("PROD", "green");
                }
            }
            if(CA_host_list.contains((host_okay_list.get(i)[3]))){
                Individual_CA_table.put(host_okay_list.get(i)[3], "green");
                Group_flag_table.put("CA_host_list", "green");

                if(DIT_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_DIT_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("DIT", "green");
                    Env_CA_table.put("DIT", "green");
                }
                if(PERF_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PERF_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PERF", "green");
                    Env_CA_table.put("PERF", "green");
                }
                if(Stage_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_Stage_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("Stage", "green");
                    Env_CA_table.put("Stage", "green");
                }
                if(PROD_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PROD_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PROD", "green");
                    Env_CA_table.put("PROD", "green");
                }
            }
            if(EIS_host_list.contains(host_okay_list.get(i)[3])){
                Individual_EIS_table.put(host_okay_list.get(i)[3], "green");
                Group_flag_table.put("EIS_host_list", "green");

                if(DIT_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_DIT_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("DIT", "green");
                    Env_EIS_table.put("DIT", "green");
                }
                if(PERF_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PERF_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PERF", "green");
                    Env_EIS_table.put("PERF", "green");
                }
                if(Stage_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_Stage_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("Stage", "green");
                    Env_EIS_table.put("Stage", "green");
                }
                if(PROD_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PROD_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PROD", "green");
                    Env_EIS_table.put("PROD", "green");
                }
            }
            if(IS_host_list.contains(host_okay_list.get(i)[3])){
                Individual_IS_table.put(host_okay_list.get(i)[3], "green");
                Group_flag_table.put("IS_host_list" ,"green");

                if(DIT_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_DIT_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("DIT", "green");
                    Env_IS_table.put("DIT", "green");
                }
                if(PERF_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PERF_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PERF", "green");
                    Env_IS_table.put("PERF", "green");
                }
                if(Stage_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_Stage_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("Stage", "green");
                    Env_IS_table.put("Stage", "green");
                }
                if(PROD_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PROD_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PROD", "green");
                    Env_IS_table.put("PROD", "green");
                }
            }
            if(PIS_host_list.contains(host_okay_list.get(i)[3])){
                Individual_PIS_table.put(host_okay_list.get(i)[3], "green");
                Group_flag_table.put("PIS_host_list", "green");

                if(DIT_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_DIT_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("DIT", "green");
                    Env_PIS_table.put("DIT", "green");
                }
                if(PERF_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PERF_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PERF", "green");
                    Env_PIS_table.put("PERF", "green");
                }
                if(Stage_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_Stage_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("Stage", "green");
                    Env_PIS_table.put("Stage", "green");
                }
                if(PROD_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PROD_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PROD", "green");
                    Env_PIS_table.put("PROD", "green");
                }
            }
            if(Polling_host_list.contains(host_okay_list.get(i)[3])){
                Individual_Polling_table.put(host_okay_list.get(i)[3], "green");
                Group_flag_table.put("Polling_host_list", "green");

                if(DIT_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_DIT_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("DIT", "green");
                    Env_Polling_table.put("DIT", "green");
                }
                if(PERF_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PERF_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PERF", "green");
                    Env_Polling_table.put("PERF", "green");
                }
                if(Stage_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_Stage_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("Stage", "green");
                    Env_Polling_table.put("Stage", "green");
                }
                if(PROD_host_list.contains(host_okay_list.get(i)[3])){
                    Individual_PROD_table.put(host_okay_list.get(i)[3], "green");
                    Env_group_table.put("PROD", "green");
                    Env_Polling_table.put("PROD", "green");
                }
            }

            else{
                //System.out.println("Incorrect host name");
            }
            //System.out.println("Okay: " + Arrays.toString(host_okay_list.get(i)));
        }

        for (int i = 0; i < host_warning_list.size(); i++) {
            if(Test_host_list.contains(host_warning_list.get(i)[3])) {
                Group_flag_table.put("Test_host_list", "yellow");

            }
            if(Auth_host_list.contains(host_warning_list.get(i)[3])){
                Individual_Auth_table.put(host_warning_list.get(i)[3], "yellow");
                Group_flag_table.put("Auth_host_list", "yellow");

                if(DIT_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_DIT_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("DIT", "yellow");
                    Env_Auth_table.put("DIT", "yellow");
                }
                if(PERF_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PERF_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PERF", "yellow");
                    Env_Auth_table.put("PERF", "yellow");
                }
                if(Stage_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_Stage_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("Stage", "yellow");
                    Env_Auth_table.put("Stage", "yellow");
                }
                if(PROD_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PROD_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PROD", "yellow");
                    Env_Auth_table.put("PROD", "yellow");
                }
            }
            if(CA_host_list.contains((host_warning_list.get(i)[3]))){
                Individual_CA_table.put(host_warning_list.get(i)[3], "yellow");
                Group_flag_table.put("CA_host_list", "yellow");

                if(DIT_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_DIT_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("DIT", "yellow");
                    Env_CA_table.put("DIT", "yellow");
                }
                if(PERF_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PERF_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PERF", "yellow");
                    Env_CA_table.put("PERF", "yellow");
                }
                if(Stage_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_Stage_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("Stage", "yellow");
                    Env_CA_table.put("Stage", "yellow");
                }
                if(PROD_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PROD_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PROD", "yellow");
                    Env_CA_table.put("PROD", "yellow");
                }
            }
            if(EIS_host_list.contains(host_warning_list.get(i)[3])){
                Individual_EIS_table.put(host_warning_list.get(i)[3], "yellow");
                Group_flag_table.put("EIS_host_list", "yellow");

                if(DIT_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_DIT_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("DIT", "yellow");
                    Env_EIS_table.put("DIT", "yellow");
                }
                if(PERF_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PERF_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PERF", "yellow");
                    Env_EIS_table.put("PERF", "yellow");
                }
                if(Stage_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_Stage_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("Stage", "yellow");
                    Env_EIS_table.put("Stage", "yellow");
                }
                if(PROD_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PROD_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PROD", "yellow");
                    Env_EIS_table.put("PROD", "yellow");
                }
            }
            if(IS_host_list.contains(host_warning_list.get(i)[3])){
                Individual_IS_table.put(host_warning_list.get(i)[3], "yellow");
                Group_flag_table.put("IS_host_list" ,"yellow");

                if(DIT_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_DIT_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("DIT", "yellow");
                    Env_IS_table.put("DIT", "yellow");
                }
                if(PERF_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PERF_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PERF", "yellow");
                    Env_IS_table.put("PERF", "yellow");
                }
                if(Stage_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_Stage_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("Stage", "yellow");
                    Env_IS_table.put("Stage", "yellow");
                }
                if(PROD_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PROD_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PROD", "yellow");
                    Env_IS_table.put("PROD", "yellow");
                }
            }
            if(PIS_host_list.contains(host_warning_list.get(i)[3])){
                Individual_PIS_table.put(host_warning_list.get(i)[3], "yellow");
                Group_flag_table.put("PIS_host_list", "yellow");

                if(DIT_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_DIT_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("DIT", "yellow");
                    Env_PIS_table.put("DIT", "yellow");
                }
                if(PERF_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PERF_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PERF", "yellow");
                    Env_PIS_table.put("PERF", "yellow");
                }
                if(Stage_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_Stage_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("Stage", "yellow");
                    Env_PIS_table.put("Stage", "yellow");
                }
                if(PROD_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PROD_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PROD", "yellow");
                    Env_PIS_table.put("PROD", "yellow");
                }
            }
            if(Polling_host_list.contains(host_warning_list.get(i)[3])){
                Individual_Polling_table.put(host_warning_list.get(i)[3], "yellow");
                Group_flag_table.put("Polling_host_list", "yellow");

                if(DIT_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_DIT_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("DIT", "yellow");
                    Env_Polling_table.put("DIT", "yellow");
                }
                if(PERF_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PERF_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PERF", "yellow");
                    Env_Polling_table.put("PERF", "yellow");
                }
                if(Stage_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_Stage_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("Stage", "yellow");
                    Env_Polling_table.put("Stage", "yellow");
                }
                if(PROD_host_list.contains(host_warning_list.get(i)[3])){
                    Individual_PROD_table.put(host_warning_list.get(i)[3], "yellow");
                    Env_group_table.put("PROD", "yellow");
                    Env_Polling_table.put("PROD", "yellow");
                }

            }
            else{
                //System.out.println("Incorrect host name");
            }
            //System.out.println("Warning: " + Arrays.toString(host_warning_list.get(i)));
        }


        for (int i = 0; i < host_critical_list.size(); i++) {
            if(Test_host_list.contains(host_critical_list.get(i)[3])) {
                Group_flag_table.put("Test_host_list", "red");

            }
            if(Auth_host_list.contains(host_critical_list.get(i)[3])){
                Individual_Auth_table.put(host_critical_list.get(i)[3], "red");
                Group_flag_table.put("Auth_host_list", "red");

                if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("DIT", "red");
                    Env_Auth_table.put("DIT", "red");
                }
                if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PERF", "red");
                    Env_Auth_table.put("PERF", "red");
                }
                if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("Stage", "red");
                    Env_Auth_table.put("Stage", "red");
                }
                if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PROD", "red");
                    Env_Auth_table.put("PROD", "red");
                }
            }
            if(CA_host_list.contains((host_critical_list.get(i)[3]))){
                Individual_CA_table.put(host_critical_list.get(i)[3], "red");
                Group_flag_table.put("CA_host_list", "red");

                if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("DIT", "red");
                    Env_CA_table.put("DIT", "red");
                }
                if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PERF", "red");
                    Env_CA_table.put("PERF", "red");
                }
                if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("Stage", "red");
                    Env_CA_table.put("Stage", "red");
                }
                if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PROD", "red");
                    Env_CA_table.put("PROD", "red");
                }
            }
            if(EIS_host_list.contains(host_critical_list.get(i)[3])){
                Individual_EIS_table.put(host_critical_list.get(i)[3], "red");
                Group_flag_table.put("EIS_host_list", "red");

                if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("DIT", "red");
                    Env_EIS_table.put("DIT", "red");
                }
                if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PERF", "red");
                    Env_EIS_table.put("PERF", "red");
                }
                if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("Stage", "red");
                    Env_EIS_table.put("Stage", "red");
                }
                if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PROD", "red");
                    Env_EIS_table.put("PROD", "red");
                }
            }
            if(IS_host_list.contains(host_critical_list.get(i)[3])){
                Individual_IS_table.put(host_critical_list.get(i)[3], "red");
                Group_flag_table.put("IS_host_list" ,"red");

                if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("DIT", "red");
                    Env_IS_table.put("DIT", "red");
                }
                if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PERF", "red");
                    Env_IS_table.put("PERF", "red");
                }
                if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("Stage", "red");
                    Env_IS_table.put("Stage", "red");
                }
                if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PROD", "red");
                    Env_IS_table.put("PROD", "red");
                }
            }
            if(PIS_host_list.contains(host_critical_list.get(i)[3])){
                Individual_PIS_table.put(host_critical_list.get(i)[3], "red");
                Group_flag_table.put("PIS_host_list", "red");

                if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("DIT", "red");
                    Env_PIS_table.put("DIT", "red");
                }
                if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PERF", "red");
                    Env_PIS_table.put("PERF", "red");
                }
                if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("Stage", "red");
                    Env_PIS_table.put("Stage", "red");
                }
                if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PROD", "red");
                    Env_PIS_table.put("PROD", "red");
                }
            }
            if(Polling_host_list.contains(host_critical_list.get(i)[3])){
                Individual_Polling_table.put(host_critical_list.get(i)[3], "red");
                Group_flag_table.put("Polling_host_list", "red");

                if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("DIT", "red");
                    Env_Polling_table.put("DIT", "red");
                }
                if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PERF", "red");
                    Env_Polling_table.put("PERF", "red");
                }
                if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("Stage", "red");
                    Env_Polling_table.put("Stage", "red");
                }
                if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                    Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                    Env_group_table.put("PROD", "red");
                    Env_Polling_table.put("PROD", "red");
                }

            }
            /*if(DIT_host_list.contains(host_critical_list.get(i)[3])){
                Individual_DIT_table.put(host_critical_list.get(i)[3], "red");
                Env_group_table.put("DIT", "red");
            }
            if(PERF_host_list.contains(host_critical_list.get(i)[3])){
                Individual_PERF_table.put(host_critical_list.get(i)[3], "red");
                Env_group_table.put("PERF", "red");
            }
            if(Stage_host_list.contains(host_critical_list.get(i)[3])){
                Individual_Stage_table.put(host_critical_list.get(i)[3], "red");
                Env_group_table.put("Stage", "red");
            }
            if(PROD_host_list.contains(host_critical_list.get(i)[3])){
                Individual_PROD_table.put(host_critical_list.get(i)[3], "red");
                Env_group_table.put("PROD", "red");
            }*/
            else{
                //System.out.println("Incorrect host name");
            }
            //System.out.println("Critical: " + Arrays.toString(host_critical_list.get(i)));
        }
        System.out.println("Host okay total: " + host_okay_list.size());
        System.out.println("Host critical total: " + host_critical_list.size());
        System.out.println("Host warning total: " + host_warning_list.size());
        System.out.println("Group table: " + Group_flag_table);
        System.out.println("Individual table: " + Individual_flag_table);

        System.out.println("Flag: " + clear_flag);

        if(clear_flag == true){



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
                    t.put("group", "Authorization");
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
                    t.put("group", "Enterprise Identity Service");
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
                    t.put("group", "Product Identity Service");
                    t.put("name", key);
                    t.put("color", Individual_DIT_table.get(key));
                    DIT_list.add(t);
                }
                if(Polling_host_list.contains(key)){
                    JSONObject t = new JSONObject();
                    t.put("group", "Polling");
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
                    t.put("group", "Authorization");
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
                    t.put("group", "Enterprise Identity Service");
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
                    t.put("group", "Product Identity Service");
                    t.put("name", key);
                    t.put("color", Individual_PERF_table.get(key));
                    PERF_list.add(t);
                }
                if(Polling_host_list.contains(key)){
                    JSONObject t = new JSONObject();
                    t.put("group", "Polling");
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
                    t.put("group", "Authorization");
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
                    t.put("group", "Enterprise Identity Service");
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
                    t.put("group", "Product Identity Service");
                    t.put("name", key);
                    t.put("color", Individual_Stage_table.get(key));
                    Stage_list.add(t);
                }
                if(Polling_host_list.contains(key)){
                    JSONObject t = new JSONObject();
                    t.put("group", "Polling");
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
                    t.put("group", "Authorization");
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
                    t.put("group", "Enterprise Identity Service");
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
                    t.put("group", "Product Identity Service");
                    t.put("name", key);
                    t.put("color", Individual_PROD_table.get(key));
                    PROD_list.add(t);
                }
                if(Polling_host_list.contains(key)){
                    JSONObject t = new JSONObject();
                    t.put("group", "Polling");
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
                    t.put("name", "Authorization");
                    t.put("color", Env_Auth_table.get(key));
                    DIT_detail_list.add(t);
                }
                else if(key.equals("PERF")){
                    t.put("name", "Authorization");
                    t.put("color", Env_Auth_table.get(key));
                    PERF_detail_list.add(t);
                }
                else if(key.equals("Stage")){
                    t.put("name", "Authorization");
                    t.put("color", Env_Auth_table.get(key));
                    Stage_detail_list.add(t);
                }
                else if(key.equals("PROD")){
                    t.put("name", "Authorization");
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
                    t.put("name", "Enterprise Identity Service");
                    t.put("color", Env_EIS_table.get(key));
                    DIT_detail_list.add(t);
                }
                else if(key.equals("PERF")){
                    t.put("name", "Enterprise Identity Service");
                    t.put("color", Env_EIS_table.get(key));
                    PERF_detail_list.add(t);
                }
                else if(key.equals("Stage")){
                    t.put("name", "Enterprise Identity Service");
                    t.put("color", Env_EIS_table.get(key));
                    Stage_detail_list.add(t);
                }
                else if(key.equals("PROD")){
                    t.put("name", "Enterprise Identity Service");
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
                    t.put("name", "Product Identity Service");
                    t.put("color", Env_PIS_table.get(key));
                    DIT_detail_list.add(t);
                }
                else if(key.equals("PERF")){
                    t.put("name", "Product Identity Service");
                    t.put("color", Env_PIS_table.get(key));
                    PERF_detail_list.add(t);
                }
                else if(key.equals("Stage")){
                    t.put("name", "Product Identity Service");
                    t.put("color", Env_PIS_table.get(key));
                    Stage_detail_list.add(t);
                }
                else if(key.equals("PROD")){
                    t.put("name", "Product Identity Service");
                    t.put("color", Env_PIS_table.get(key));
                    PROD_detail_list.add(t);
                }

            }
            Enumeration<String> e18 = Env_Polling_table.keys();
            while (e18.hasMoreElements()) {
                JSONObject t = new JSONObject();
                String key = e18.nextElement();
                if(key.equals("DIT")){
                    t.put("name", "Polling");
                    t.put("color", Env_Polling_table.get(key));
                    DIT_detail_list.add(t);
                }
                else if(key.equals("PERF")){
                    t.put("name", "Polling");
                    t.put("color", Env_Polling_table.get(key));
                    PERF_detail_list.add(t);
                }
                else if(key.equals("Stage")){
                    t.put("name", "Polling");
                    t.put("color", Env_Polling_table.get(key));
                    Stage_detail_list.add(t);
                }
                else if(key.equals("PROD")){
                    t.put("name", "Polling");
                    t.put("color", Env_Polling_table.get(key));
                    PROD_detail_list.add(t);
                }

            }

            System.out.println("TTTTT: " + Auth_list);

            obj7.put("Env_list", Env_list);
            obj7.put("DIT_list", DIT_list);
            obj7.put("PERF_list", PERF_list);
            obj7.put("Stage_list", Stage_list);
            obj7.put("PROD_list", PROD_list);
            obj7.put("Authorization", Auth_list);
            obj7.put("Common Admin", CA_list);
            obj7.put("Enterprise Identity Service", EIS_list);
            obj7.put("Identity Service", IS_list);
            obj7.put("Product Identity Service", PIS_list);
            obj7.put("Polling", Polling_list);
            obj7.put("DIT", DIT_detail_list);
            obj7.put("PERF", PERF_detail_list);
            obj7.put("Stage", Stage_detail_list);
            obj7.put("PROD", PROD_detail_list);
            //obj7.put("Details", Test_list);
            Master_list.add(obj7);
            obj8.put("Individual_lists", Master_list);


            System.out.println("Returned: " + obj8);
            print_lists();
            /*System.out.println("EIS List: " + EIS_list);
            System.out.println("EIS List: " + EIS_list.size());
            System.out.println("EIS Host List: " + EIS_host_list);
            System.out.println("EIS Host List: " + EIS_host_list.size());
            System.out.println("EIS Table List: " + Individual_EIS_table);
            System.out.println("EIS Table List: " + Individual_EIS_table.size());*/
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
            host_okay_list.clear();
            host_warning_list.clear();
            host_critical_list.clear();
            Temp_host_list.clear();


            clear_flag = false;

        }
        else {
            clear_flag = true;

        }

        System.out.println("Flag: " + clear_flag);


        return obj8;
    }

    private static void print_lists() {
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
        System.out.println("DIT List: " + DIT_host_list);
        System.out.println("DIT List: " + DIT_host_list.size());
        System.out.println("PERF List: " + PERF_host_list);
        System.out.println("PERF List: " + PERF_host_list.size());
        System.out.println("Stage List: " + Stage_host_list);
        System.out.println("Stage List: " + Stage_host_list.size());
        System.out.println("PROD List: " + PROD_host_list);
        System.out.println("PROD List: " + PROD_host_list.size());

    }

}






