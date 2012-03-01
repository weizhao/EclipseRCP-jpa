package org.eclipse.examples.jpa.persistence.service;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;

public abstract class PersistenceService {
    private EntityManagerFactory _emf;
    private EntityManager _em;
    private boolean _isManaged;
    private ThreadLocal<EntityManager> _thread = new ThreadLocal<EntityManager>();
    public PersistenceService(){
    }
    public PersistenceService(EntityManagerFactory emf){
    	this();
    	if(emf==null){
    		throw new NullPointerException();
    	}
    	_emf = emf;
    }
    public PersistenceService(String PU_NAME){
    	this();
    	_emf = getEntityManagerFactory(PU_NAME);
    }
    
    public void injectEntityManager(EntityManager em){
    	_isManaged = true;
    	_em = em;
    }
    private EntityManager getEntityManager(){
    	EntityManager em = getEM();
    	if(em==null){
    		throw new NullPointerException();
    	}
    	return em;
    }
    private EntityManager getEM(){
    	if(_em != null){
    		return _em;
    	}
    	if(_thread == null){
    		_thread = new ThreadLocal<EntityManager>();
    	}
    	EntityManager em = _thread.get();
    	if(em==null||!em.isOpen()){
    		em = _emf.createEntityManager();
    		_thread.set(em);
    	}
    	return em;
    }
    public EntityManager beginTransaction(){
    	EntityManager em = getEntityManager();
    	if(!_isManaged&&em!=null){
    		em.getTransaction().begin();
    	}
    	return em;
    }
    public void commitTransaction(){
    	if(_isManaged){
    		return;
    	}
    	EntityManager em = getEntityManager();
    	if(em!=null&&em.getTransaction().isActive()){
    		em.getTransaction().commit();
    		em.close();
    	}
    }
    public void rollbackTransaction(){
    	if(_isManaged){
    		return;
    	}
    	EntityManager em = getEntityManager();
    	if(em!=null&&em.getTransaction().isActive()){
    		em.getTransaction().rollback();
    		em.close();
    	}
    }
    private EntityManagerFactory getEntityManagerFactory(String PU_NAME) {
        if (_emf == null) {
            HashMap properties = new HashMap();
            properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
            _emf = new PersistenceProvider().createEntityManagerFactory(PU_NAME,properties);
        }
        return _emf;
    }

}
