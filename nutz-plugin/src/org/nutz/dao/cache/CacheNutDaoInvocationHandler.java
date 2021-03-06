/**
 * 
 */
package org.nutz.dao.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.nutz.dao.Dao;
import org.nutz.dao.cache.method.ClearMethodHandler;
import org.nutz.dao.cache.method.DeleteMethodHandler;
import org.nutz.dao.cache.method.FetchMethodHandler;
import org.nutz.dao.cache.method.IDaoCacheMethodHandler;
import org.nutz.dao.cache.method.InsertMethodHandler;
import org.nutz.dao.cache.method.UpdateMethodHandler;
import org.nutz.dao.convent.utils.CommonUtils;
import org.nutz.dao.entity.EntityMaker;
import org.nutz.dao.entity.impl.ConventionEntityMaker;

/**
 * 缓存实现的主要处理器
 * 1.建议Dao接口添加setEntityMaker方法
 * 2.建议Dao接口getEntityHolder方法,以及EntityHolder中添加getMappings方法
 * @author liaohongliu
 *
 * 创建时间: 2011-4-21
 */
public class CacheNutDaoInvocationHandler implements InvocationHandler {

	private Dao dao;
	private EntityMaker entityMaker;
	private Cache cache;
	/**
	 * 存储DAO中方法对应的缓存处理器
	 * key:dao中的方法名
	 * value:对应的处理器
	 */
	private static Map<String,IDaoCacheMethodHandler> DAO_METHOD_HANDLERS=new HashMap<String,IDaoCacheMethodHandler>();
	
	static{
		DAO_METHOD_HANDLERS.put("delete", new DeleteMethodHandler());
		DAO_METHOD_HANDLERS.put("deleteWith", new DeleteMethodHandler());
		DAO_METHOD_HANDLERS.put("deleteLinks", new DeleteMethodHandler());
		DAO_METHOD_HANDLERS.put("deletex", new DeleteMethodHandler());
		
		DAO_METHOD_HANDLERS.put("fetch", new FetchMethodHandler());
		DAO_METHOD_HANDLERS.put("fetchx", new FetchMethodHandler());
		DAO_METHOD_HANDLERS.put("fetchLinks", new FetchMethodHandler());
		
		DAO_METHOD_HANDLERS.put("insert", new InsertMethodHandler());
		DAO_METHOD_HANDLERS.put("fastInsert", new InsertMethodHandler());
		DAO_METHOD_HANDLERS.put("insertWith", new InsertMethodHandler());
		DAO_METHOD_HANDLERS.put("insertLinks", new InsertMethodHandler());
		DAO_METHOD_HANDLERS.put("insertRelation", new InsertMethodHandler());
		
		DAO_METHOD_HANDLERS.put("update", new UpdateMethodHandler());
		DAO_METHOD_HANDLERS.put("updateIgnoreNull", new UpdateMethodHandler());
		DAO_METHOD_HANDLERS.put("updateWith", new UpdateMethodHandler());
		DAO_METHOD_HANDLERS.put("updateLinks", new UpdateMethodHandler());
		DAO_METHOD_HANDLERS.put("updateRelation", new UpdateMethodHandler());
		
		DAO_METHOD_HANDLERS.put("clear", new ClearMethodHandler());
		DAO_METHOD_HANDLERS.put("clearLinks", new ClearMethodHandler());
	}
	public CacheNutDaoInvocationHandler(Dao dao) {
		super();
		this.dao = dao;
		this.cache=new HashtableCache(dao.toString());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(entityMaker!=null){
			CommonUtils.invokeMethod(dao, "setEntityMaker", new Class[]{EntityMaker.class}, new Object[]{new ConventionEntityMaker()});
		}
		CacheStrategy cacheStrategy=new CacheStrategy();
		cacheStrategy.setDao(dao);
		ObsArgClass msg=new ObsArgClass(method,args,cacheStrategy,cache);
		IDaoCacheMethodHandler daoHandler=DAO_METHOD_HANDLERS.get(method.getName());
		if(daoHandler!=null){
			return daoHandler.handler(msg);
		}
		return method.invoke(dao, args);
	}
	
	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public EntityMaker getEntityMaker() {
		return entityMaker;
	}
	public void setEntityMaker(EntityMaker entityMaker) {
		this.entityMaker = entityMaker;
	}


}
