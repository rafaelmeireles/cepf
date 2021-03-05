package br.com.rmc.security.role;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;

@Entity
@Table(schema="security")
public class Role extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5017824520579753974L;
	
	@NotNull
	@Column(nullable=false, unique = true)	
	private String code;
	
	@NotNull
	@Column(nullable=false, unique = true)	
	private String name;
	
	@NotNull
	@Column(nullable=false)
	private Boolean active;
		
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
}