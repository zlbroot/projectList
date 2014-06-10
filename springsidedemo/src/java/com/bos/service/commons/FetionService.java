package com.bos.service.commons;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bos.kit.ext.FetionWap;

@Service
@Transactional
public class FetionService {
	private ParamsService paramsService;

	@Resource
	public void setParamsService(ParamsService paramsService) {
		this.paramsService = paramsService;
	}

	Map<String, FetionWap> fwMap = new HashMap<String, FetionWap>();

	public String send(String loginName, String msg) {
		FetionWap t = fwMap.get(loginName);
		if (t == null) {
			Map<String, String> aa = paramsService
					.getParams2MapByType("fetion_" + loginName);
			t = new FetionWap();
			t.setUn(aa.get("un"));
			t.setPwd(aa.get("pwd"));
		}
		return t.send(msg);
	}
}
