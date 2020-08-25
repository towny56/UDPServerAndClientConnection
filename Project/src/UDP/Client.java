package UDP;

import java.net.*;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) throws Exception
    {
        DatagramSocket clientSocket = new DatagramSocket(); // MAKE SOCKET FOR THE CLIENT

        InetAddress ipAddress = InetAddress.getByName("127.15.56.1"); // GETS THE ID OF THE HOST;

        byte[] sendData = new byte[1024]; // BYTES FOR THE SEND DATA
        byte[] receiveData = new byte[1024]; // BYTES FOR THE RECEIVED DATA

        System.out.println("FROM CLIENT\nInsert name of the file: ");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        fileName = ".\\" + fileName + ".txt"; //MAKES THE FULL PATH OF THE FILE

        sendData = fileName.getBytes("UTF-8"); //TRANSFER DATA IN BYTES TO SEND

        DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length, ipAddress, 9256); //MAKE PACKET TO SEND

        clientSocket.send(sendPacket); // SEND PACKET TO SERVER BY THE CLIENT SOCKET

        DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length); // MAKE PACKET TO RECEIVE

        clientSocket.receive(receivePacket); // RECEIVE PACKET FORM SERVER BY THE CLIENT SOCKET

        String information = new String(receivePacket.getData());

        System.out.println("FROM SERVER\n" + information);

        clientSocket.close(); // CLOSE THE OPENED CLIENT SOCKET
    }
}
