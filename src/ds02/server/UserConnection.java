package ds02.server;

import java.net.Socket;

import ds02.server.event.DisconnectedEvent;
import ds02.server.event.EventBus;
import ds02.server.event.EventHandler;
import ds02.server.event.LogoutEvent;
import ds02.server.event.handler.DefaultEventHandler;
import ds02.server.util.AuctionProtocolStream;

public class UserConnection {

	private final EventBus<DisconnectedEvent> onClose;
	private final EventBus<LogoutEvent> onLogout;
	private final Socket tcpSocket;
	private final AuctionProtocolStream protocolStream;
	private String username;

	public UserConnection(Socket tcpSocket) {
		this.onClose = new EventBus<DisconnectedEvent>();
		this.onLogout = new EventBus<LogoutEvent>();
		this.tcpSocket = tcpSocket;

		try {
			this.protocolStream = new AuctionProtocolStream(tcpSocket.getOutputStream(), tcpSocket.getInputStream());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void writeResponse(String response) {
		protocolStream.write(response);
	}

	public String readRequest() {
		return protocolStream.read();
	}

	public String getUsername() {
		return username;
	}

	public void login(final String username) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("Invalid username");
		}

		if (isLoggedIn()) {
			throw new IllegalStateException("Already logged in");
		}

		try {
			this.username = username;

			/* Make sure the user is logged out when the connection is closed */
			addCloseListener(new EventHandler<DisconnectedEvent>() {
				@Override
				public void handle(DisconnectedEvent event) {
					UserConnection.this.logout();
					DefaultEventHandler.INSTANCE.handle(event);
				}
			});
		} catch (Exception ex) {
			throw new IllegalArgumentException("Could not connect to client",
					ex);
		}
	}

	public void logout() {
		if (isLoggedIn()) {
			Throwable t = null;

			try {
				onLogout.notify(new LogoutEvent(username));
			} catch (Throwable ex) {
				t = ex;
			}

			/*
			 * Always remove the handlers, otherwise we will create a memory
			 * leak
			 */
			onLogout.removeHandlers();
			username = null;

			if (t != null) {
				/* Checked exceptions are not possible */
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				} else if (t instanceof Error) {
					throw (Error) t;
				}
			}
		}
	}

	public boolean isLoggedIn() {
		return username != null;
	}

	public void addLogoutListener(EventHandler<LogoutEvent> handler) {
		onLogout.addHandler(handler);
	}

	public void addCloseListener(EventHandler<DisconnectedEvent> handler) {
		onClose.addHandler(handler);
	}

	public void close() {
		if (!tcpSocket.isClosed()) {
			Throwable t = null;

			if (username != null) {
				try {
					onClose.notify(new DisconnectedEvent(username));
				} catch (Throwable ex) {
					t = ex;
				}

				username = null;
			}

			/*
			 * Always remove the handlers, otherwise we will create a memory
			 * leak
			 */
			onClose.removeHandlers();

			try {
				tcpSocket.close();
			} catch (Exception ex) {
				// Ignore
			}

			if (t != null) {
				/* Checked exceptions are not possible */
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				} else if (t instanceof Error) {
					throw (Error) t;
				}
			}
		}
	}
}
