package com.example.sendmsg;

public class Person {

	private String name;
	private String gender;
	private String platform;
	private String phoneNumber;
	private int send;
	private String repaymoney;
	private String status;

	public Person() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRepaymoney() {
		return repaymoney;
	}

	public void setRepaymoney(String repaymoney) {
		this.repaymoney = repaymoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getSend() {
		return send;
	}

	public void setSend(int send) {
		this.send = send;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", gender=" + gender + ", platform="
				+ platform + ", phoneNumber=" + phoneNumber + ", send=" + send
				+ ", repaymoney=" + repaymoney + ", status=" + status + "]";
	}

}
