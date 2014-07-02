package com.test.dao;

import com.test.entity.User;
import com.zlb.core.annos.dao.DaoRepository;
import com.zlb.core.dao.CrudRepository;

@DaoRepository(clazz = User.class)
public interface UserDao extends CrudRepository<User, Long>{
}
