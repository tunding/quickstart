package org.springside.fi.service.running;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

@Transactional
public class BaseService {
	/**
	 * 创建分页请求.
	 */
	public PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		try{
			String[] sortProperties = sortType.split(" ");
			sort = new Sort(Direction.fromString(sortProperties[1]), sortProperties[0]);
		} catch (Exception e) {
            sort = new Sort(Direction.ASC,"id");
        }

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	protected <T> Specification<T> buildSpecification(Map<String, Object> searchParams, Class<T> clazz) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), clazz);
		return spec;
	}
	/**
	 * @param time
	 * @return 将String类型的time转换成Date类型
	 */
	public Date TransferDate(String time){
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
		Date starttime = null;
		try {
			starttime = df.parse(time);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return starttime;
	}
}
