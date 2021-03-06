package com.sd.pallentloan.pojo;

import java.util.Random;

import com.sd.pallentloan.utils.TimeUtils;

public class PersonForHome implements Comparable{

	private String time;
	private Long timeMills;
	private String name;
	private int money;
	private Random random = new Random();

	public PersonForHome() {
		super();
		name = firstName.charAt(random.nextInt(firstName.length())) + "";
		money = borrowMoney[random.nextInt(borrowMoney.length)];
		timeMills = System.currentTimeMillis() - random.nextInt(18)*100000;
		time = TimeUtils.parseDate3(timeMills);
	}

	public PersonForHome(String time, String name, int money) {
		super();
		this.time = time;
		this.name = name;
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoney() {
		return money;
	}

	public long getTimeMills() {
		return timeMills;
	}

	public void setTimeMills(long timeMills) {
		this.timeMills = timeMills;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return "PersonForHome [time=" + time + ", name=" + name + ", money="
				+ money + "]";
	}

	String firstName = "赵钱孙李周吴郑王赵钱孙李周吴郑王赵钱孙李周吴郑王冯陈楮卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闽席季麻强贾路娄危江童颜郭梅盛林刁锺徐丘骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麹家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘斜厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍郤璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终居衡步都耿满弘匡国文寇广禄阙东欧沃利蔚越隆师巩聂晁勾敖融冷訾辛阚那简饶空曾沙养鞠须丰关蒯相查后荆红游竺权逑盖益桓公万";
	int [] borrowMoney = {500, 700, 1000, 1200, 1500, 2000, 5000, 1200, 1500, 2000, 5000, 2000, 5000, 5000};

	@Override
	public int compareTo(Object another) {
		PersonForHome person = (PersonForHome) another;
		long otherTime = person.getTimeMills();
		return this.timeMills.compareTo(otherTime);
	}

}
