package UDP;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPMax;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class Server
{
    private static List<Information> LIST_INFOMATION; // THE LIST WITH THE ALL INFORMATION FROM THE FILE
    private static List<Information> RESULT; // THE INFORMATION WITCH IS GONNA BE SEND FOR THE CLIENT
    private static List<String> NAME_LINE; // THE FIRST LINE OF THE TABLE WITH THE NAMES OF THE COLUMNS
    private static Map<String, Pair<Integer, Integer>> MAP_INFORMATION; // THE MAP WITH ALL STRING THAT ARE NOT REPEATED
    private static Map<String, Integer> ANALIZE; // MAKES MAP FROM THE RESULT OF THE ALGORITHM TO SEE WITCH IS THE BIGGEST NUMBER ASS LINE

    private static Integer COUNTER = 1;
    private static Integer MAXIMUM = 0;

    private static final Integer DATE = 1;
    private static final Integer TIME = 2;
    private static final Integer EVENTCONTEXT = 3;
    private static final Integer COMPONENT = 4;
    private static final Integer EVENTNAME = 5;
    private static final Integer DESCRIPTION = 6;
    private static final Integer ORIGIN = 7;
    private static final Integer IPADDRESS = 8;

    public static void main(String[] args) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(9256); // MAKE SOCKET FOR THE SERVER

        byte[] receiveData = new byte[1024]; // BYTES FOR THE RECEIVED DATA
        byte[] sendData = new byte[1024]; // BYTES FOR THE SEND DATA

        while (true)
        {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); // MAKE PACKET TO RECEIVE

            serverSocket.receive(receivePacket); // RECEIVE PACKET FORM CLIENT BY THE SERVER SOCKET

            String fileName = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");

            InetAddress ipAddress = receivePacket.getAddress(); // GETS THE ID OF THE HOST BY THE RECEIVED PACKET
            int port = receivePacket.getPort(); // GETS THE PORT OF THE HOST BY THE RECEIVED PACKET

            makeListWithInformation(fileName); // MAKE LIST WITH ALL INFORMATION FROM THE FILE
            makeMapWithInformation(); // MAKE THE MAP WITH ALL INFORMATION AND PRINTS NUMBERS IN FILE WITCH THE ALGORITHM WILL USE
            useAlgorithm(); // USE THE ALGORITHM
            analyzeTheResultOfALgorithm(); // ANALYZE THE RESULT FROM ALGORITHM
            findMaxNumberForAction(); // FINDS THE MAXIMUM ACTION WITCH IS USED
            makeTheResult(); // MAKE LIST OF INFORMATION WITH THE MOST USED ACTIONS

            String result = makeStringToSendForTheClient();

            sendData = result.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port); // SEND INFORMATION TO THE CLIENT

            serverSocket.send(sendPacket); // CLOSE THE SURVER SOCKET
        }
    }

    private static void makeListWithInformation (String fileName) throws Exception
    {
        BufferedReader buffer = new BufferedReader(new FileReader(fileName));

        String line = buffer.readLine();

        NAME_LINE = Arrays.asList(line.split(","));

        LIST_INFOMATION = new ArrayList<>();

        while ((line = buffer.readLine()) != null)
        {
            String[] lineArray = line.split(",");

            if (lineArray.length == 8)
            {
                LIST_INFOMATION.add(new Information(lineArray[0].substring(1), lineArray[1].substring(1, lineArray[1].length() - 1), lineArray[2], lineArray[3], lineArray[4], lineArray[5], lineArray[6], lineArray[7]));
            }
        }
    }

    private static void makeMapWithInformation () throws Exception
    {
        MAP_INFORMATION = new HashMap<>();
        FileWriter writer = new FileWriter(".\\source.txt");

        for (Information element : LIST_INFOMATION)
        {
            List<Integer> currentLine = new ArrayList<>();

            currentLine.add(getNumberFromMap(element.getDate(), DATE));
            currentLine.add(getNumberFromMap(element.getTime(), TIME));
            currentLine.add(getNumberFromMap(element.getEventContext(), EVENTCONTEXT));
            currentLine.add(getNumberFromMap(element.getComponent(), COMPONENT));
            currentLine.add(getNumberFromMap(element.getEventName(), EVENTNAME));
            currentLine.add(getNumberFromMap(element.getDescription(), DESCRIPTION));
            currentLine.add(getNumberFromMap(element.getOrigin(), ORIGIN));
            currentLine.add(getNumberFromMap(element.getIpAddress(), IPADDRESS));

            currentLine.sort((x,y) -> (x - y));

            String currentLineOutput = "";

            for (Integer currentInteger : currentLine)
            {
                currentLineOutput = currentLineOutput + Integer.toString(currentInteger) + " ";
            }

            currentLineOutput = currentLineOutput + "\r\n";

            writer.write(currentLineOutput);
        }

        writer.close();
    }

    public static Integer getNumberFromMap (String string, int column)
    {
        Integer result = 0;

        if (MAP_INFORMATION.containsKey(string))
        {
            result = MAP_INFORMATION.get(string).getKey();
        }
        else
        {
            MAP_INFORMATION.put(string, new Pair<>(COUNTER, column));
            result = COUNTER;
            COUNTER = COUNTER + 1;
        }

        return result;
    }

    public static void useAlgorithm () throws Exception
    {
        AlgoFPMax algorithm = new AlgoFPMax();
        algorithm.runAlgorithm(".\\source.txt", ".\\result.txt", 0.0);
        algorithm.printStats();
    }

    public static void analyzeTheResultOfALgorithm () throws Exception
    {
        ANALIZE = new HashMap<String, Integer>();

        BufferedReader buffer = new BufferedReader(new FileReader(".\\result.txt"));

        String line = "";
        String before = "";
        String after = "";

        while ((line = buffer.readLine()) != null)
        {
            Integer index = line.indexOf('#');

            before = line.substring(0, index);
            after = line.substring(index + 6);

            Integer number = Integer.parseInt(after);

            if (!ANALIZE.containsKey(before))
            {
                ANALIZE.put(before, number);
            }
        }
    }

    public static void findMaxNumberForAction () throws Exception
    {
        for (Map.Entry<String, Integer> current : ANALIZE.entrySet())
        {
            if (MAXIMUM < current.getValue())
            {
                MAXIMUM = current.getValue();
            }
        }
    }

    public static void makeTheResult ()
    {
        RESULT = new ArrayList<Information>();

        for (Map.Entry<String, Integer> current : ANALIZE.entrySet())
        {
            if (current.getValue().equals(MAXIMUM))
            {
                RESULT.add(convertStringOfNumbers(current.getKey()));
            }
        }
    }

    public static Information convertStringOfNumbers (String string)
    {
        String[] elements = string.split(" ");

        Information information = new Information();

        for (String current : elements)
        {
            Integer column = findColumn(Integer.parseInt(current)); // FIND THE COLUMN FOR THE UNIQUE NUMBER FROM THE ORIGINAL INFORMATION
            String sentence = findString(Integer.parseInt(current)); // FIND THE STRING FOR THE UNIQUE NUMBER FROM THE ORIGINAL INFORMATION

            switch (column)
            {
                case 1: information.setDate(sentence); break;
                case 2: information.setTime(sentence); break;
                case 3: information.setEventContext(sentence); break;
                case 4: information.setComponent(sentence); break;
                case 5: information.setEventName(sentence); break;
                case 6: information.setDescription(sentence); break;
                case 7: information.setOrigin(sentence); break;
                case 8: information.setIpAddress(sentence); break;
            }
        }

        return information;
    }

    public static Integer findColumn (Integer number)
    {
        Integer column = 1;

        for (Map.Entry<String, Pair<Integer, Integer>> current : MAP_INFORMATION.entrySet())
        {
            if (current.getValue().getKey().equals(number))
            {
                column = current.getValue().getValue();
            }
        }

        return column;
    }

    public static String findString (Integer number)
    {
        String sentance = "";

        for (Map.Entry<String, Pair<Integer, Integer>> current : MAP_INFORMATION.entrySet())
        {
            if (current.getValue().getKey().equals(number))
            {
                sentance = current.getKey();
            }
        }

        return sentance;
    }

    private static String makeStringToSendForTheClient ()
    {
        String result = "";

        String dates = "";
        String times = "";
        String eventContexts = "";
        String components = "";
        String eventNames = "";
        String descriptions = "";
        String origins = "";
        String ipAddresses = "";

        for (Information element : RESULT)
        {
            dates = dates + element.getDate() + "; ";
            times = times + element.getTime() + "; ";
            eventContexts = eventContexts + element.getEventContext() + "; ";
            components = components + element.getComponent() + "; ";
            eventNames = eventNames + element.getEventName() + "; ";
            descriptions = descriptions + element.getDescription() + "; ";
            origins = origins + element.getOrigin() + "; ";
            ipAddresses = ipAddresses + element.getIpAddress() + "; ";
        }

        result = String.format("INFORMATION FROM THE ANALIZATION\nThe most rushed dates are: %s\nThe most rushed times are: %s\nThe most used event contexts are: %s\nThe most used components are: %s\nThe most used events names are: %s\nThe most used descriptions are: %s\nThe most used origins are: %s\nThe most active ip addresses are: %s\n",
                               dates, times, eventContexts, components, eventNames, descriptions, origins, ipAddresses);

        return result;
    }
}
