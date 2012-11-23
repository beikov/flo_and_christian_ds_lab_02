package ds02.server.service;

import java.io.Serializable;

import ds02.server.UserConnection;
import ds02.server.service.impl.UserServiceImpl;

public interface UserService extends Serializable {

	public static final UserService INSTANCE = new UserServiceImpl();

	public UserConnection getUser(String username);

	public boolean login(String username, UserConnection userConnection);

	public void logout(String username);

}