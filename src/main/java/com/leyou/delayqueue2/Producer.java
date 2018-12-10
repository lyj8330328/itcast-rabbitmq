package com.leyou.delayqueue2;

import cn.itcast.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;

/**
 * @Author: 98050
 * @Time: 2018-12-10 20:11
 * @Feature:
 */
public class Producer {

    private static String queue_name = "message_ttl_queue";

    public static void main(String[] args) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(queue_name, true, false, false, null);
        // 绑定路由
        channel.queueBind(queue_name, "amq.direct", "message_ttl_routingKey");

        String message = System.currentTimeMillis() + "";
        // 设置延时属性
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        // 持久性 non-persistent (1) or persistent (2)
        AMQP.BasicProperties properties = builder.expiration("60000").deliveryMode(2).build();
        // routingKey =delay_queue 进行转发
        channel.basicPublish("", "delay_queue", properties, message.getBytes());
        System.out.println("sent message: " + message + ",date:" + System.currentTimeMillis());
        // 关闭频道和连接
        channel.close();
        connection.close();

    }
}
