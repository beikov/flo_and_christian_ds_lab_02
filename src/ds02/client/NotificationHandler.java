package ds02.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class NotificationHandler implements Runnable {

    private static final int BUFFER_SIZE = 2048;
    private final int udpPort;
    private DatagramSocket socket;
    /* User may be changed by main thread, so we need to make it volatile to avoid synchronization */
    private volatile String user;

    public NotificationHandler(int udpPort) {
        this.udpPort = udpPort;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void stop() {
        if(socket != null && !socket.isClosed()){
            socket.close();
        }
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(udpPort);
        } catch (Exception ex) {
            throw new RuntimeException("Could not set up notification handler!", ex);
        }
        
        while (socket != null && !socket.isClosed()) {
            DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);

            try {
                socket.receive(packet);
                final String notification = new String(packet.getData(), 0, packet.getLength());
                final String[] notificationParts = notification.split("\\s+");
                final StringBuilder sb = new StringBuilder();
                /* Volatile read */
                final String username = user;

                if (notification.startsWith("!new-bid ") && notificationParts.length > 1) {
                    sb.append("You have been overbid on '");

                    for (int i = 1; i < notificationParts.length; i++) {
                        if (i != 1) {
                            sb.append(' ');
                        }

                        sb.append(notificationParts[i]);
                    }

                    sb.append('\'');
                } else if (notification.startsWith("!auction-ended ") && notificationParts.length > 3) {
                    sb.append("The auction '");

                    for (int i = 3; i < notificationParts.length; i++) {
                        if (i != 3) {
                            sb.append(' ');
                        }

                        sb.append(notificationParts[i]);
                    }

                    sb.append("' has ended. ");

                    if (username.equals(notificationParts[1])) {
                        sb.append("You won with ");
                        sb.append(notificationParts[2]);
                        sb.append("!");
                    } else {
                        sb.append(notificationParts[1]);
                        sb.append(" won with ");
                        sb.append(notificationParts[2]);
                        sb.append(".");
                    }
                } else {
                    sb.append("Unknown notification: '");
                    sb.append(notification);
                    sb.append("'");
                }
				
				/* Trying to overcome the limitation of the access to the native console */

				System.out.write('\r');
                System.out.println(sb.toString());
				System.out.flush();
				
                System.out.print(username);
                System.out.print("> ");
				System.out.flush();
            } catch (Exception ex) {
                // Don't care about the errors since logging is not required
                // if(!socket.isClosed()){
                //     ex.printStackTrace(System.err);
                // }
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                // Ignore
            }
        }
    }
}
