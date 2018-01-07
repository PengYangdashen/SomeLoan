package com.example.sendoverdatamsg;

import java.io.Serializable;

public class Person implements Serializable {

	private String name;
	private double repaymoney;// 借款
	private int overdue;// 逾期
	private String Msg;
	private String userId;
	private String mobilePhone;
	private double interest;// 利息

	public Person() {
		super();
	}

	public Person(String name, double repaymoney, int overdue, String msg,
			String userId, String mobilePhone, double interest) {
		super();
		this.name = name;
		this.repaymoney = repaymoney;
		this.overdue = overdue;
		Msg = msg;
		this.userId = userId;
		this.mobilePhone = mobilePhone;
		this.interest = interest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRepaymoney() {
		return repaymoney;
	}

	public void setRepaymoney(double repaymoney) {
		this.repaymoney = repaymoney;
	}

	public int getOverdue() {
		return overdue;
	}

	public void setOverdue(int overdue) {
		this.overdue = overdue;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", repaymoney=" + repaymoney
				+ ", overdue=" + overdue + ", Msg=" + Msg + ", userId="
				+ userId + ", mobilePhone=" + mobilePhone + ", interest="
				+ interest + "]";
	}

}
