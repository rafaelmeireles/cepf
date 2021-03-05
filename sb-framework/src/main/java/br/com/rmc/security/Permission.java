package br.com.rmc.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.com.rmc.BaseEntity;

@Entity
@Table(schema="security", name="permission")
public class Permission extends BaseEntity {

	private static final long serialVersionUID = -5017824520579753974L;
	
	public static final String READ_CODE = "read";
	public static final String WRITE_CODE = "write";
	public static final String READ_NAME = "Read";
	public static final String WRITE_NAME = "Write";
	
	private String code;
	private Permission father;
	private String name;
	private Boolean active;
	
	@NotNull
	@Column(nullable=false, unique = true)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_permission_father")	
	public Permission getFather() {
		return father;
	}
	
	public void setFather(Permission father) {
		this.father = father;
	}

	@NotNull
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Column(nullable=false)
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Transient
	public boolean isRead() {
		return getCode().endsWith(READ_CODE);
	}
	
	@Transient
	public boolean isWrite() {
		return getCode().endsWith(WRITE_CODE);
	}	
	
	@PrePersist
	public void prePersist() {
		setActive(true);
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
}