package com.zlb.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pager<T> {
	private int curPage = 1; // 当前页
	private int pageSize = 10; // 每页多少行
	private int totalRow; // 共多少行
	private int start;// 当前页起始行
	private int end;// 结束行
	private int totalPage; // 共多少页

	private String orderBy = "";
	private Map<String, Object> params = new HashMap<String, Object>();

	private List<T> content = new ArrayList<T>();

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		if (curPage < 1) {
			curPage = 1;
		} else {
			start = pageSize * (curPage - 1);
		}
		end = start + pageSize > totalRow ? totalRow : start + pageSize;
		this.curPage = curPage;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		totalPage = (totalRow + pageSize - 1) / pageSize;
		this.totalRow = totalRow;
		if (totalPage < curPage) {
			curPage = totalPage;
			start = pageSize * (curPage - 1);
			end = totalRow;
		}
		end = start + pageSize > totalRow ? totalRow : start + pageSize;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		if (null != content) {
			this.content = content;
		}
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}