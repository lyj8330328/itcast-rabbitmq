package com.leyou.delayqueue;

import cn.itcast.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author: 98050
 * @Time: 2018-12-10 14:26
 * @Feature:
 */
public class Consumer {

    private static String QUEUE_NAME = "test.queue";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 定义队列的消费者
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
        channel.basicConsume(QUEUE_NAME, true,consumer);
    }
}
