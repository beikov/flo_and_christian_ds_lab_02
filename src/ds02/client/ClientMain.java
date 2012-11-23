package ds02.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientMain {

    public static void main(String[] args) {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        /* Socket connection to server */
        final Socket socket;
        /* Runnable for async notification receive */
        final NotificationHandler notificationHandler;
        /* Using this one since we might have multiline responses */
        final ObjectInputStream socketReader;
        final BufferedWriter socketWriter;

        if (args.length != 3) {
            usage();
        }

        String host = args[0];
        int tcpPort = 0;
        int udpPort = 0;

        try {
            tcpPort = Integer.parseInt(args[1]);
            udpPort = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            ex.printStackTrace(System.err);
            usage();
        }

        try {
            socket = new Socket(host, tcpPort);
            socketReader = new ObjectInputStream(socket.getInputStream());
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception ex) {
            throw new RuntimeException("Could not connect to server", ex);
        }
        
        notificationHandler = new NotificationHandler(udpPort);

        String user = null;
        String command;
        String prompt = "> ";

        while (true) {
            try {
                command = prompt(prompt, in);
            } catch (Exception ex) {
                command = null;
            }

            if (command == null || "!end".equals(command)) {
                break;
            }

            final String[] commandParts = command.split("\\s+");
            final boolean loginCommand = "!login".equals(commandParts[0]);
            final boolean logoutCommand = "!logout".equals(commandParts[0]);

            try {
                socketWriter.write(command);

                if (loginCommand) {
                    /* If no args are given, pass empty argument to get the right error message */
                    if(commandParts.length < 2){
                        socketWriter.write(' ');
                    }
                    /* We add the udp port to the login message */
                    socketWriter.write(' ');
                    socketWriter.write("" + udpPort);

                    if (user == null && commandParts.length > 1) {
                        /* Extract user name out of the command so we can show it in the prompt and also start the notification handler */
                        user = commandParts[1];
                        notificationHandler.setUser(user);
                        new Thread(notificationHandler).start();
                    }
                }
                
                socketWriter.newLine();
                socketWriter.flush();

                /* 
                 * In case we get a ClassCastException, we don't care since we
                 * have to terminate the connection and so on anyway
                 */
                final String result = (String) socketReader.readObject();

                if (logoutCommand || (loginCommand && !result.contains("Successfully") && user != null)) {
                    /* Here we have either a logout or an unsuccessful login */
                    user = null;
                    prompt = "> ";
                    notificationHandler.stop();
                } else if (user != null) {
                    /* Successful login */
                    prompt = user + "> ";
                }

                System.out.println(result);
            } catch (Exception ex) {
                System.out.println("Server terminated connection");
                
                break;
            }
        }

        Runnable shutdownHook = new Runnable() {

            @Override
            public void run() {
                
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception ex1) {
                        // Ignore
                    }
                }

                notificationHandler.stop();
            }
        };
        
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
        
        shutdownHook.run();
    }

    private static void usage() {
        System.out.println("Usage: " + ClientMain.class.getSimpleName() + " <host> <tcpPort> <udpPort>");
        System.exit(1);
    }

    public static String prompt(String prompt, BufferedReader in) throws Exception {
		System.out.print(prompt);
		System.out.flush();
        return in.readLine();
    }
}
