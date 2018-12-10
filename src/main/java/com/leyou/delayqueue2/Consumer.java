package com.leyou.delayqueue2;

import java.io.IOException;
import java.util.HashMap;

import cn.itcast.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

/**
 * @Author: 98050
 * @Time: 2018-12-10 20:15
 * @Feature:
 */

public class Consumer {

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

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                // body 即消息体
                String msg = new String(body);
                System.out.println(" [消费者] received : " + msg);
                Long result = System.currentTimeMillis() - Long.parseLong(msg);
                System.out.println("当前时间：" + System.currentTimeMillis());
                System.out.println("间隔：" + result/1000);
            }
        };

        channel.basicConsume(queue_name, true,consumer);
    }

}
