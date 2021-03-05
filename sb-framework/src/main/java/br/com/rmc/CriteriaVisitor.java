package br.com.rmc;

import org.hibernate.Criteria;

/**
 * Implementação do Visitor Pattern para permitir manipulação do
 * Criteria criado nos métodos find da classe BaseService
 * acrescentando novos critérios de busca que não estão necessáriamente na
 * entidade da busca (Ex: intervalo de datas, intervalo de valores,etc)
 * 
 * @author Rafael Meireles
 * 
 */
public abstract class CriteriaVisitor {

	public void visit(Criteria criteria) {
	}
	
	public void visit(String property, Criteria criteria) {
	}

}
