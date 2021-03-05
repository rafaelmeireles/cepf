package br.com.rmc.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.rmc.BaseEntity;

public class NotNullSpecification<T extends BaseEntity> implements Specification<T> {
	
	private static final long serialVersionUID = -2440333332080006218L;
	
	private String field = null;
	
	public NotNullSpecification(String field) {
		this.field = field;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.isNotNull(root.get(field)));
		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
