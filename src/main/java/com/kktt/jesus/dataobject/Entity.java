package com.kktt.jesus.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 实体公共类,封装部分公共属性
 * 
 * @author hongqinggong
 * @since 2017年4月27日
 */
public abstract class Entity implements Serializable {
	private static final long serialVersionUID = -6290462705444569297L;
	private int  id;
	private int  status;
	private Long createTime;
	private Long updateTime;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	@JsonIgnore
	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
}