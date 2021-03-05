package br.com.rmc.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.rmc.BaseEntity;

public class JoinSpecification<T extends BaseEntity, E extends BaseEntity> implements Specification<T> {
	
	private static final long serialVersionUID = -2440333332080006218L;
	
	private String joinField = null;
	private E joinEntity = null;
	
	public JoinSpecification(String joinField, E joinEntity) {
		this.joinField = joinField;
		this.joinEntity = joinEntity;
	}
	
	
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (this.joinField != null && !this.joinField.isEmpty() && this.joinEntity != null && this.joinEntity.getId() != null) {
			Join<T, E> joinE = root.join(joinField);
			return builder.equal(joinE.get("id"), joinEntity.getId());
		}
		return null;
	}
}
