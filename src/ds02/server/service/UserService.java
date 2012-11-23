package ds02.server.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ds02.server.UserConnection;

public class UserService {
    
    public static final UserService INSTANCE = new UserService();
    private final ConcurrentMap<String, UserConnection> users = new ConcurrentHashMap<String, UserConnection>();
    
    public UserConnection getUser(String username){
        checkUsername(username);
        return users.get(username);
    }
    
    public boolean login(String username, UserConnection userConnection){
        checkUsername(username);
        return users.putIfAbsent(username, userConnection) == null;
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
