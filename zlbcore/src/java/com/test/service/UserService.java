package com.test.service;

import com.test.dao.UserDao;
import com.test.entity.User;
import com.zlb.core.annos.Resource;
import com.zlb.core.annos.service.Service;

@Service
public class UserService {
	private UserDao userDao;

	@Resource
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public User findOne(Long id) {
		return userDao.findOne(id);
	}
}
