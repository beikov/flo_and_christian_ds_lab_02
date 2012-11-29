package ds02.server.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.ConnectException;
import java.rmi.Remote;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReconnectInterceptor implements InvocationHandler {

	private final String remoteName;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private Remote instance;
	
	public ReconnectInterceptor(String remoteName) {
		this.remoteName = remoteName;
		this.instance = RegistryUtils.getRemote(remoteName);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		lock.readLock().lock();
		
		try{
			return method.invoke(instance, args);
		} catch(InvocationTargetException ex){
			if(ex.getTargetException() instanceof ConnectException){
				lock.readLock().unlock();
				lock.writeLock().lock();
				
				Remote reconnectedInstance;
				
				try{
					reconnectedInstance = RegistryUtils.getRemote(remoteName);
					
					if(reconnectedInstance != null){
						instance = reconnectedInstance;
					}
				} catch(Exception e1){
					/* We can't do anything here*/
				} finally {
					lock.writeLock().unlock();
				}
				
				lock.readLock().lock();
				
				return method.invoke(instance, args);
			}
			
			throw ex;
		} finally {
			lock.readLock().unlock();
		}
	}

}
