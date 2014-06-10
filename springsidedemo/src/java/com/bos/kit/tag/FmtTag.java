package com.bos.kit.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.springframework.util.StringUtils;

import com.bos.entity.Params;
import com.bos.kit.JsonKit;
import com.bos.kit.SpringKit;
import com.bos.service.commons.ParamsService;

public class FmtTag extends BaseTag {
	private static final long serialVersionUID = -810642816423804702L;
	private String dictionaryType;// 字典类型，即tb_ucpaas_params.param_type字段
	private String data;// 直接传入的数据，json格式，如[{value:'1',text:'文本1'},{value:'2',text:'文本2'}]
	private String value = "";// 选中的值
	public FmtTag() {
		freeMarkerName = "fmt.ftl";
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
	public int doStartTag() throws JspException {
		String result = "";
		if(!StringUtils.isEmpty(value)){
			if (!StringUtils.isEmpty(dictionaryType)) {// 查询数据字典表
				List<Params>  list = getParamsService().getTagDataForDictionary(dictionaryType, null);
				for (Params params : list) {
					if(value.equals(params.getK())){
						result = params.getV();
					}
				}
			} else if (!StringUtils.isEmpty(data)) {// 静态数据
				List <Map<String,Object>>list = JsonKit.json2Object(data, List.class);
				for (Map<String, Object> map : list) {
					if(value.equals(map.get("k"))){
						result =String.valueOf(map.get("v"));
					}
				}
			}
		}
		try {
			pageContext.getOut().println(result);
		} catch (Exception e) {
			throw new JspException("SelectParamsTag: " + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
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
}