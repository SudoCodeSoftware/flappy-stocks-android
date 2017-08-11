package com.sudo_code.flappystocks;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class NetworkingTask extends AsyncTask<GameView, Double, Object> {

    private GameView mGameView;

    @Override
    protected Object doInBackground(GameView... gameView) {
        mGameView = gameView[0];

        try {
            final int serverPort = 5149;
            final int clientPort = 5149;
            final String serverAddress = "192.168.0.20";

            Random random = new Random(System.currentTimeMillis());
            long clientId = random.nextLong();

            InetAddress IPAddress = InetAddress.getByName(serverAddress);

            byte[] sendData = new byte[8];
            ByteBuffer.wrap(sendData).order(ByteOrder.BIG_ENDIAN).putLong(clientId).array();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);

            DatagramSocket clientSocket = new DatagramSocket(clientPort);
            clientSocket.setReuseAddress(true);
            clientSocket.setSoTimeout(1000);    //1000 milliseconds wait for clientSocket.receive
            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[8];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (true) {
                try {
                    clientSocket.receive(receivePacket);
                    double newPrice = ByteBuffer.wrap(receivePacket.getData()).getDouble();
                    publishProgress(newPrice);
                } catch(SocketTimeoutException | NullPointerException e) {
                    e.printStackTrace();
                }
            }

            //clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Double... pricePoint) {
        mGameView.getPriceData().add(pricePoint[0]);
        mGameView.setLastPriceTime(System.currentTimeMillis());

        if (mGameView.getPriceData().size() > mGameView.getMaxPriceDataSize()) {
            mGameView.getPriceData().removeFirst();
        }
    }
}
