package com.bos.kit.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bos.kit.JsonKit;
import com.bos.kit.SpringKit;
import com.bos.service.commons.ParamsService;

public class SelectTag extends BaseTag {
	private static final long serialVersionUID = -810642816423804702L;
	private String id = "";// html的id、name标记
	private String dictionaryType;// 字典类型，即tb_ucpaas_params.param_type字段
	private String dictionaryKeys;// 字典键值，即tb_ucpaas_params.param_key字段，多个值用,分割
	private String data;// 直接传入的数据，json格式，如[{value:'1',text:'文本1'},{value:'2',text:'文本2'}]
	private String value = "";// 选中的值
	private String callback = "";// 选择后的回调函数，两个参数value、text
	private String placeholder = "";// 占位符
	private String all;// 占位符
	public SelectTag() {
		freeMarkerName = "select.ftl";
	}

	private ParamsService paramsService;

	public ParamsService getParamsService() {
		if(paramsService == null){
			paramsService = SpringKit.getBean(ParamsService.class);
		}
		return paramsService;
	}

	@Override
	@SuppressWarnings("all")
	protected Map<String, Object> doProcess() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("value", value);
		params.put("placeholder", placeholder);
		params.put("callback", callback);
		params.put("all", false);
		if("true".equalsIgnoreCase(all)){
			params.put("all", true);
		}
		List list = new ArrayList();
		if (!StringUtils.isEmpty(dictionaryType)) {// 查询数据字典表

			List<String> keys = null;
			if (!StringUtils.isEmpty(dictionaryKeys)) {
				keys = Arrays.asList(dictionaryKeys.split(","));
			}

			list = getParamsService().getTagDataForDictionary(dictionaryType, keys);
		} else if (!StringUtils.isEmpty(data)) {// 静态数据
			list = JsonKit.json2Object(data, List.class);
		}
		params.put("list", list);
		return params;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDictionaryType() {
		return dictionaryType;
	}

	public void setDictionaryType(String dictionaryType) {
		this.dictionaryType = dictionaryType;
	}

	public String getDictionaryKeys() {
		return dictionaryKeys;
	}

	public void setDictionaryKeys(String dictionaryKeys) {
		this.dictionaryKeys = dictionaryKeys;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getAll() {
		return all;
	}

	public void setAll(String all) {
		this.all = all;
	}
	

}