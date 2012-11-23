package ds02.client;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import ds02.server.event.Event;
import ds02.server.service.BillingServiceSecure;

public class UserContext {

	private String username = null;
	private Set<String> subscriptions = new HashSet<String>();
	private boolean auto;
	private Set<Event> eventSet = new LinkedHashSet<Event>();
	private BillingServiceSecure billingServiceSecure;
	
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
		removeSubscription(subscription);
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	public void addEvent(Event event) {
		eventSet.add(event);
	}
	
	public Set<Event> popEventQueue() {
		Set<Event> tempEvents = eventSet;
		eventSet = new LinkedHashSet<Event>();
		return tempEvents;
	}

	public String getUsername() {
		return username;
	}
	

}
