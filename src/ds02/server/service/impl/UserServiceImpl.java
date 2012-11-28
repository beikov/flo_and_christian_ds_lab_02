package ds02.server.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ds02.server.UserConnection;
import ds02.server.event.EventHandler;
import ds02.server.event.LoginEvent;
import ds02.server.event.LogoutEvent;
import ds02.server.event.handler.DefaultEventHandler;
import ds02.server.service.UserService;

public class UserServiceImpl implements UserService {

	private static final long serialVersionUID = 1L;
	private final ConcurrentMap<String, UserConnection> users = new ConcurrentHashMap<String, UserConnection>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.UserService#getUser(java.lang.String)
	 */
	@Override
	public UserConnection getUser(String username) {
		checkUsername(username);
		return users.get(username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.UserService#login(java.lang.String,
	 * ds02.server.UserConnection)
	 */
	@Override
	public boolean login(String username, UserConnection userConnection) {
		checkUsername(username);

		if (users.putIfAbsent(username, userConnection) == null) {
			DefaultEventHandler.INSTANCE.handle(new LoginEvent(username));
			userConnection.addLogoutListener(new EventHandler<LogoutEvent>() {

				@Override
				public void handle(LogoutEvent event) {
					UserServiceImpl.this.logout(event.getUser());
				}
			});

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ds02.server.service.impl.UserService#logout(java.lang.String)
	 */
	@Override
	public void logout(String username) {
		checkUsername(username);
		users.remove(username);
		DefaultEventHandler.INSTANCE.handle(new LogoutEvent(username));
	}

	private static void checkUsername(String username) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("Invalid username");
		}
	}
}
