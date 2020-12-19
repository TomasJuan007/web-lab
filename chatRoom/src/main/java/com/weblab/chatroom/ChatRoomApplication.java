package com.weblab.chatroom;

import com.weblab.chatroom.handler.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatRoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatRoomApplication.class,args);
        try {
            System.out.println("http://127.0.0.1:8080/netty-websocket/index");
            new NettyServer(10000).start();
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }
}
