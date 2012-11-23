package ds01.server.event;

public interface EventHandler<T> {
    
    public void handle(T event);
}
