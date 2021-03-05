package br.com.rmc.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.rmc.BaseEntity;

public class OrSpecification<T extends BaseEntity> implements Specification<T> {
	
	private static final long serialVersionUID = -2440333332080006218L;
	
	private String field = null;
	private List<?> listOr = null;
	
	public OrSpecification(String field, List<?> listOr) {
		this.field = field;
		this.listOr = listOr;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (listOr != null) {
			listOr.stream().forEach( orField -> {
				predicates.add(builder.equal(root.get(field), orField));
			});
		} else {
			return null;
		}

		return builder.or(predicates.toArray(new Predicate[predicates.size()]));
	}
}
