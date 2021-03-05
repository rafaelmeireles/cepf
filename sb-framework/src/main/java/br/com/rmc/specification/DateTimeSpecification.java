package br.com.rmc.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.rmc.BaseEntity;

public class DateTimeSpecification<T extends BaseEntity> implements Specification<T> {
	
	private static final long serialVersionUID = -2440333332080006218L;
	
	private String field = null;
	private LocalDateTime dataInicial = null;
	private LocalDateTime dataFinal = null;
	
	public DateTimeSpecification(String field, LocalDateTime dataInicial, LocalDateTime dataFinal) {
		this.field = field;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (dataInicial != null) {
			predicates.add(builder.and(builder.greaterThanOrEqualTo(root.get(field), dataInicial)));
		}

		if (dataFinal != null) {
			predicates.add(builder.and(builder.lessThanOrEqualTo(root.get(field), dataFinal)));
		}

		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
