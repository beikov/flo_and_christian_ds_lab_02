package ds02.client;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import ds02.server.event.Event;
import ds02.server.service.BillingServiceSecure;

public class UserContext {

	private final PrintStream out;
	private final Set<String> subscriptions = new HashSet<String>();
	private Set<Event> eventSet = new LinkedHashSet<Event>();
	private BillingServiceSecure billingServiceSecure;
	private String username = null;
	private boolean auto;

	public UserContext(PrintStream out) {
		this.out = out;
	}

	public void login(String username, BillingServiceSecure billingServiceSecure) {
		this.username = username;
		this.billingServiceSecure = billingServiceSecure;
	}

	public void logout() {
		this.username = null;
		this.billingServiceSecure = null;
	}

	public boolean isLoggedIn() {
		return (username != null && billingServiceSecure != null);
	}

	public void addSubscription(String subscription) {
		subscriptions.add(subscription);
	}

	public void removeSubscription(String subscription) {
		subscriptions.remove(subscription);
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public void addEvent(Event event) {
		if (!isAuto()) {
			eventSet.add(event);
		} else {
			out.println(event.toString());
		}
	}

	public Set<Event> popEventQueue() {
		Set<Event> tempEvents = eventSet;
		eventSet = new LinkedHashSet<Event>();
		return tempEvents;
	}

	public String getUsername() {
		return username;
	}

	public BillingServiceSecure getBillingServiceSecure() {
		return billingServiceSecure;
	}
	
	public PrintStream getOut(){
		return out;
	}

}
