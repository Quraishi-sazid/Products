package com.example.hishab.hishabApp.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tbl_user_month_mapping")
@AllArgsConstructor
@NoArgsConstructor
public class UserMonthMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userMonthMappingId;
	private int year;
	private int month;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public UserModel user;
	public UserMonthMapping(int year,int month,UserModel userModel)
	{
		this.year = year;
		this.month = month;
		this.user = userModel;
	}
	
}

