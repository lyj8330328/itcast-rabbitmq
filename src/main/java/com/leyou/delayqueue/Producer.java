package com.leyou.delayqueue;

import cn.itcast.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.Calendar;

/**
 * @Author: 98050
 * @Time: 2018-12-10 14:15
 * @Feature:
 */
public class Producer {

    private final static String EXCHANGE_NAME = "delaysync.exchange";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 发布消息
        String message = System.currentTimeMillis() +"";
        channel.basicPublish(EXCHANGE_NAME,"deal.message" ,null ,message.getBytes());
        System.out.println("sent message：" + message + ",date:" + System.currentTimeMillis());
        // 关闭通道和连接
        channel.close();
        connection.close();
    }
}
