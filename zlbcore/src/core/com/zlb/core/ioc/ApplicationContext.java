package com.zlb.core.ioc;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zlb.core.annos.Resource;
import com.zlb.core.annos.dao.DaoImpl;
import com.zlb.core.annos.dao.DaoRepository;
import com.zlb.core.annos.mvc.Action;
import com.zlb.core.annos.mvc.Controller;
import com.zlb.core.annos.service.Service;
import com.zlb.core.dao.CrudRepository;
import com.zlb.core.dao.impl.CrudRepositoryInvocationHandler;
import com.zlb.core.dao.impl.SimpleCrudRepository;
import com.zlb.core.kit.GenericsKit;
import com.zlb.core.kit.StrKit;
import com.zlb.core.logger.Logger;

public class ApplicationContext {
	public ConcurrentHashMap<String, Bean> beanFactory = new ConcurrentHashMap<String, Bean>();
	protected static Logger log = Logger.getLogger(ApplicationContext.class);
	private BeanCreatedCallback beanCreatedCallback;

	public ApplicationContext(BeanCreatedCallback beanCreatedCallback) {
		this.beanCreatedCallback = beanCreatedCallback;
	}

	/**
	 * 初始化入口
	 * 
	 * @param classLst
	 * @param scan
	 */
	public void init(Set<String> classLst, String scan) {
		Class<?> clazz = null;
		for (String className : classLst) {
			if (className.startsWith(scan)) {
				try {
					clazz = Class.forName(className);
					initClazz(clazz);
				} catch (ClassNotFoundException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		initSetResource();
	}

	/**
	 * 执行依赖关系的建立
	 */
	private void initSetResource() {
		Class<?> clazz = null;
		Class<?> pclazz = null;
		Resource resource = null;
		Object params = null;
		for (Bean bean : beanFactory.values()) {
			clazz = bean.clazz;
			for (Method m : clazz.getMethods()) {
				resource = m.getAnnotation(Resource.class);
				if (null != resource) {
					params = null;
					if (StrKit.isBlank(resource.value())) {
						pclazz = m.getParameterTypes()[0];
						for (Bean b2 : beanFactory.values()) {
							if (b2.clazz.isAssignableFrom(pclazz)) {
								params = b2.object;
								break;
							}
						}
					} else {
						params = beanFactory.get(resource.value());
					}
					if (null != params) {
						try {
							m.invoke(bean.object, params);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					} else {
						log.error("connot do[" + clazz.getName() + "."
								+ m.getName() + "]  at " + resource);
					}
				}
			}
		}
	}

	/**
	 * 初始化单个Bean
	 * 
	 * @param clazz
	 */
	private void initClazz(Class<?> clazz) {
		Bean bean = null;
		Action action = clazz.getAnnotation(Action.class);
		Controller controller = clazz.getAnnotation(Controller.class);
		Service service = clazz.getAnnotation(Service.class);
		DaoImpl dao = clazz.getAnnotation(DaoImpl.class);
		String id = null;
		if (null != controller) {
			id = controller.value();
		} else if (null != service) {
			id = service.value();
		} else if (null != dao) {
			id = dao.value();
		} else if (null != action) {
			id = "";
		}
		if (null != id) {
			if (StrKit.isBlank(id)) {
				id = StrKit.firstCharToLowerCase(clazz.getSimpleName());
			}
			bean = addBean(clazz, id);
			beanCreatedCallback.callback(bean);
		}
		DaoRepository daoRepository = clazz.getAnnotation(DaoRepository.class);
		if (null != daoRepository) {
			initDaoRepository(daoRepository, clazz);
		}
	}

	/**
	 * 初始化接口dao
	 * 
	 * @param daoRepository
	 * @param clazz
	 */
	@SuppressWarnings("all")
	private void initDaoRepository(DaoRepository daoRepository, Class<?> clazz) {
		if (clazz.isInterface() && CrudRepository.class.isAssignableFrom(clazz)) {// 配置需要注意继承CrudRepository
			Class<?> entityClazz = daoRepository.clazz();
			ClassLoader classLoader = GenericsKit.class.getClassLoader();
			CrudRepository repository = new SimpleCrudRepository(entityClazz);
			addBean("repository" + Integer.toHexString(repository.hashCode()),//添加到实体管理器
					SimpleCrudRepository.class, repository);
			CrudRepositoryInvocationHandler invocationHandler = new CrudRepositoryInvocationHandler(
					repository);
			Class<?>[] interfaces = { clazz };
			Object object = Proxy.newProxyInstance(classLoader, interfaces,
					invocationHandler);
			String id = daoRepository.value();
			if (StrKit.isBlank(id)) {
				id = StrKit.firstCharToLowerCase(clazz.getSimpleName());
			}
			addBean(id, clazz, object);
		} else {
			log.debug(clazz.getName()
					+ " has DaoRepository but not extends CrudRepository or not interface");
		}
	}

	private Bean addBean(Class<?> clazz, String id) {
		if (null != id && null != clazz) {
			Bean bean = new Bean(id, clazz);
			beanFactory.put(id, bean);
			return bean;
		}
		return null;
	}

	public Bean addBean(String id, Object object) {
		if (null != object && null != id) {
			return addBean(id, object.getClass(), object);
		}
		return null;
	}

	public Bean addBean(String id, Class<?> clazz, Object object) {
		if (null != object && null != id && null != clazz) {
			Bean bean = new Bean(id, clazz, object);
			beanFactory.put(id, bean);
			return bean;
		}
		return null;
	}
}
