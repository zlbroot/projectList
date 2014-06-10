package com.bos.service.commons;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.bos.entity.Params;
import com.bos.repository.ParamsDao;

@Service
@Transactional
public class ParamsService {
	private ParamsDao paramsDao;

	@Resource
	public void setParamsDao(ParamsDao paramsDao) {
		this.paramsDao = paramsDao;
	}

	Map<String, List<Params>> cache = new HashMap<String, List<Params>>();

	public List<Params> getTagDataForDictionary(String dictionaryType,
			List<String> keys) {
		if (null == keys) {
			List<Params> result = cache.get(dictionaryType);
			if (null == result) {
				result = paramsDao.findByType(dictionaryType);
			}
			return result;
		}
		return paramsDao.findByTypeAndKIn(dictionaryType, keys);
	}
	
	public Map<String,String> getParams2MapByType(String type){
		Map<String,String> result = new HashMap<String,String>();
		List<Params> tmp = paramsDao.findByType(type);
		if(null != tmp){
			for (Params params : tmp) {
				result.put(params.getK(), params.getV());
			}
		}
		return result;
	}
	
	
	
	public Params getParams(Long id) {
		return paramsDao.findOne(id);
	}

	public void saveParams(Params entity) {
		entity.setCreated(new Date());
		paramsDao.save(entity);
	}

	public void deleteParams(Long id) {
		paramsDao.delete(id);
	}

	public List<Params> getAllParams() {
		return (List<Params>) paramsDao.findAll();
	}

	public Page<Params> getUserParams(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<Params> spec = buildSpecification(userId, searchParams);

		return paramsDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Params> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Params> spec = DynamicSpecifications.bySearchFilter(filters.values(), Params.class);
		return spec;
	}

}
