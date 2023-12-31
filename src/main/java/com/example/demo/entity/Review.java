package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rid;
	private String comment;
	private int star;
	private Date date;
	private String mainp;
	
	
	@ManyToOne
	@JoinColumn(name="gid")
	private Goods goods;
	
	@ManyToOne
	@JoinColumn(name="id")
	private User user;
	
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public double getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getMainp() {
		return mainp;
	}
	public void setMainp(String mainp) {
		this.mainp = mainp;
	}
	@Override
	public String toString() {
		return "Review [rid=" + rid + ", comment=" + comment + ", star=" + star + ", date=" + date + ", mainp=" + mainp
				+ ", goods=" + goods + ", user=" + user + "]";
	}
}
