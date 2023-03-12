package com.example.protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketConnectHelper {
    public static void main(String[] args) throws IOException {
        //创建客户端Socket对象，连接易境通网关
        Socket client = new Socket("172.x.x.x", 8116);
        //发送登陆包和定时心跳
        bootstrap(client);
        //监听服务端响应
        listen(client);
        //释放资源
        client.close();
    }

    public static void bootstrap(Socket client) throws IOException {
        DataOutputStream writer = new DataOutputStream(client.getOutputStream());
        //封装login输出流写数据
        byte[] loginPacket = getLoginPacket();
        writer.write(loginPacket);
        //封装heartbeat输出流定时写数据
        byte[] heartbeatPacket = getHeartbeatPacket();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            try {
                writer.write(heartbeatPacket);
            } catch (IOException e) {
                System.err.println("Caught IOException: " + e.getMessage());
            }
        }, 0, 20, TimeUnit.SECONDS);
    }

    public static void listen(Socket client) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String line;
            boolean exit = false;
            while (client.isConnected() && !exit) {
                if ((line = reader.readLine()) != null) {
                    System.out.println("==> bytes.length="+line.length());
                }
            }
        }
    }

    private static byte[] getLoginPacket() {
        final int length = 104;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        // 4字节
        byte[] msgType = convertIntToBytes(1);
        buffer.put(msgType);
        // 4字节，消息体长度为20+20+4+16+32=92
        byte[] bodyLength = convertIntToBytes(92);
        buffer.put(bodyLength);
        // 20字节
        byte[] senderId = getShapedBytes("***", 20);
        buffer.put(senderId);
        // 20字节
        byte[] targetId = getShapedBytes("***", 20);
        buffer.put(targetId);
        // 4字节
        byte[] hb = convertIntToBytes(60);
        buffer.put(hb);
        // 16字节
        byte[] password = getShapedBytes("***", 16);
        buffer.put(password);
        // 32字节
        byte[] version = getShapedBytes("1.02", 32);
        buffer.put(version);
        // 4字节，校验和
        byte[] checksum = getCheckSumBytes(buffer, length);
        buffer.put(checksum);
        // 获取结果
        buffer.flip();
        byte[] bytes = new byte[length];
        buffer.get(bytes, 0, length);
        return bytes;
    }

    private static byte[] getHeartbeatPacket() {
        final int length = 12;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        // 4字节
        byte[] msgType = convertIntToBytes(3);
        buffer.put(msgType);
        // 4字节
        byte[] bodyLength = convertIntToBytes(0);
        buffer.put(bodyLength);
        // 4字节，校验和
        byte[] checksum = getCheckSumBytes(buffer, length);
        buffer.put(checksum);
        // 获取结果
        buffer.flip();
        byte[] bytes = new byte[length];
        buffer.get(bytes, 0, length);
        return bytes;
    }

    private static byte[] convertIntToBytes(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    private static byte[] getShapedBytes(String input, int size) {
        if (size < input.length()) {
            System.err.println("Can't shape input!");
        }
        StringBuilder sb = new StringBuilder(input);
        for (int i = 0; i < size - input.length(); i++) {
            sb.append(" ");
        }
        String output = sb.toString();
        return output.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] getCheckSumBytes(ByteBuffer buffer, int length) {
        // 切换为读取模式
        buffer.flip();
        byte[] temp = new byte[length - 4];
        buffer.get(temp, 0, temp.length);
        // 切换回写入模式
        buffer.position(length - 4);
        buffer.limit(length);

        // 计算校验和
        int checkSum = generateCheckSum(temp, temp.length);
        return convertIntToBytes(checkSum);
    }

    private static int generateCheckSum(byte[] buf, int bufLen) {
        long idx;
        int cks;
        for (idx = 0L, cks = 0; idx < bufLen; idx++) {
            cks += buf[(int) idx];
        }
        return (cks % 256);
    }
}