
package com.gobitwheels.receiver1;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


class MyRunnable implements Runnable {        //Class for running the consumer in the different Thread
   private static final String EXCHANGE_NAME = "Exchange1";
    @Override
    public void run() {
        ConnectionFactory factory= new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        
        Connection connection=null;
        try {
            connection = factory.newConnection(); // Create connection
        } catch (IOException ex) {
            Logger.getLogger(MyRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(MyRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Channel channel= null;
        try {
            channel = connection.createChannel(); // Create Channel
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName="TestQueue"; //Consume the message from the QUEUE "TestQueue1(We have two queue, one for consuming and another for sending)
            channel.queueBind(queueName, EXCHANGE_NAME, "TestQueue");
            
            Consumer consumer= new DefaultConsumer(channel)  
        
        
         {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(message + "");
      }
    };
            
            channel.basicConsume(queueName, true, consumer);  //Consume the messages from the queue.
            
        } catch (IOException ex) {
            Logger.getLogger(MyRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
              
              
        
     
       
       
   
       
    }
}



public class receive {
    private static final String EXCHANGE_NAME = "Exchange";
     public static void main(String[] args) throws IOException, TimeoutException {
        MyRunnable runnable= new MyRunnable();
        Thread t= new Thread(runnable);
        t.start();
        ConnectionFactory factory= new ConnectionFactory(); //New connection for Sending the message on QUEUE "TestQueue1"
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        
        Connection connection= factory.newConnection();
        Channel channel= connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");        
        
        String queueName="TestQueue1";          //Queue for sending the message
        do{
        Scanner scanner= new Scanner(System.in);    
               
        String message = scanner.nextLine();    
        channel.basicPublish(EXCHANGE_NAME, queueName, null, message.getBytes()); //publish the message on the queue "TestQueue1"
      //  System.out.println(queueName +":" + message + "'");          
        

               
    
        }while(true);
    }
   


    
}
