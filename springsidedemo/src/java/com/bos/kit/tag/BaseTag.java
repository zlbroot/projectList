package com.bos.kit.tag;

import java.io.File;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 标签类的基类
 */
public abstract class BaseTag extends TagSupport {
	private static final long serialVersionUID = -7466410826954192634L;
	protected Logger logger;
	static final Configuration cfg;
	protected String freeMarkerName;
	static {
		cfg = new Configuration();
		try {
			// 设置模板文件位置
			cfg.setDirectoryForTemplateLoading(new File(BaseTag.class.getResource("/template").getFile()));
		} catch (Exception e) {
		}
	}

	public BaseTag() {
		logger = LoggerFactory.getLogger(getClass());
	}

	@Override
	public int doStartTag() throws JspException {
		Map<String, Object> rootMap = doProcess();
		try {
			Template template = cfg.getTemplate(freeMarkerName);
			template.process(rootMap, pageContext.getOut());
		} catch (Exception e) {
			throw new JspException("SelectParamsTag: " + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	protected Map<String, Object> doProcess() {
		return null;
	}

}
