package ds02.server.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ds02.server.UserConnection;
import ds02.server.event.EventHandler;
import ds02.server.event.LoginEvent;
import ds02.server.event.handler.LoginEventHandler;

public class UserService {
    
    public static final UserService INSTANCE = new UserService();
    private final ConcurrentMap<String, UserConnection> users = new ConcurrentHashMap<String, UserConnection>();
    private transient EventHandler<LoginEvent> loginHandler = new LoginEventHandler();
    
    public UserConnection getUser(String username){
        checkUsername(username);
        return users.get(username);
    }
    
    public boolean login(String username, UserConnection userConnection){
        checkUsername(username);
        
        if(users.putIfAbsent(username, userConnection) == null) {
        	loginHandler.handle(new LoginEvent(username));
        	return true;
        }
        return false;
    }
    
    public void logout(String username){
        checkUsername(username);
        users.remove(username);
    }
    
    private static void checkUsername(String username){
        if(username == null || username.isEmpty()){
            throw new IllegalArgumentException("Invalid username");
        }
    }
}
