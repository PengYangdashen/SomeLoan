package Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class testdate {

	public static void main(String[] args) {
		String ds = ""
				+ "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALPoMsWGg5gIyk/z"
				+ "Wa4wcMok3Z4fW0A1ZBnPLY3wmJ7FCzyxoTGpCIvWXtT4A73HPiZN7Qzqj8HxM0lB"
				+ "/jyy+WHtuyjSwG2cKO+IGTGI8LAV58gnV0M9PrzOBOKE6lzQF7a3L5R9u4qS7NHH"
				+ "xGTAbYAdffzyI479LawQgFE37BVBAgMBAAECgYBPOhlpzUQUZwKZVOSQhjqVesiy"
				+ "AsMPsrODfi5kjKjZepLpRpxjHzppQp1+kj4rjBu9iKG1B3MJiKv6Pfq1Rmf1z5B0"
				+ "g+ycsuvcu8WFGVmIPdNhsiybf48aKf+1JB1A1+zFXcrsiLNGd475PCQNMcfVvq5u"
				+ "6E+ir2yC6tPYY4U8hQJBAOrFoiT8Y9+go6QOsxihOum/tQ51fC4IwWoWC0tXKhxt"
				+ "GZI/SgAiljqCKst+kDq6wTlyE84dxZ9Oi+jk+NTtOwMCQQDELJUEs1SBtbSfkge7"
				+ "Nnlowqq+kFpa10T+WoyTYZsOlqLDT80kRLRrhxwB9v+kLPZOfrqcRQ6bLhNKQWsS"
				+ "oHlrAkAP3a1YjIn/We7VLn0iA/tkQqVsxbnPrp3LmpPG0qww4ZqhzI8mtS+r4pIb"
				+ "0IDUxzw5sqDuBAsP+hHwelDqquGbAkAUnv8XHGawr9IJyAbqBgLjITtjhrcIv4Iw"
				+ "HoKSZ3suIGWBlFzjCBnTB8PI7RbYQiWuAKJLFPNBGqnKb2/66EV7AkBxlcWYW0+8"
				+ "dqwun9gtueYYtnQWD/+aKD81KK2/MttY9+KVK8f3ksw8D0cGy85AoyzrivQEWNdd"
				+ "4Pvqll9NWYhS";
		System.out.println(TimeUtils.parseDate3(System.currentTimeMillis()));
		System.out.println("asdasda" + (30));
		int a = 0;
		int b = 1;
		int c = 2;
		if (a == 0 || b == 0 && c == 2) {
			
		}
		String name = "��Ǯ��������֣����Ǯ��������֣����Ǯ��������֣��������������������������ʩ�ſײ��ϻ���κ�ս���л������ˮ��������˸��ɷ�����³Τ������ﻨ������Ԭ������ﻨ������Ԭ��ۺ��ʷ�Ʒ����Ѧ�׺����������ޱϺ�������ʱ��Ƥ���뿵����Ԫ������ƽ�ƺ�������Ҧ��տ����ë����ױ���갼Ʒ�ɴ�̸��é���ܼ�������ף������������ϯ����ǿ��·¦Σ��ͯ�չ�÷ʢ�ֵ�����������Ĳ��﷮���������֧���ù�¬Ī�������Ѹɽ�Ӧ�ڶ����ڵ��������������ʯ�޼�ť�������ϻ���½��������춻����L�ҷ����ഢ�������ɾ��θ����ڽ��͹�����ɽ�ȳ������ȫۭ�����������������ﱩ��б�������������ղ����Ҷ��˾��۬�輻��ӡ�ް׻���ۢ�Ӷ����̼���׿�����ɳ����������ܲ�˫��ݷ����̷�����̼������Ƚ��۪Ӻ�S�ɣ���ţ��ͨ�����༽ۣ����ũ�±�ׯ�̲����ֳ�Ľ����ϰ�°�������������������վӺⲽ����������Ŀܹ�»�ڶ�ŷ����εԽ¡ʦ�����˹��������������Ǽ��Ŀ���ɳ������������󾣺�����Ȩ�ϸ��滸����";
		Random random = new Random();
		long time = System.currentTimeMillis() - random.nextInt(18)*100000;
		System.out.println(TimeUtils.parseDate3(time));
		
		List<PersonForHome> list = new ArrayList<PersonForHome>();
		list.add(new PersonForHome());
		list.add(new PersonForHome());
		list.add(new PersonForHome());
		list.add(new PersonForHome());
		list.add(new PersonForHome());
		Collections.sort(list);
		for (PersonForHome person : list) {
			System.out.println(person.getTimeMills() + "");
			System.out.println(person.getTime() + "");
		}
	}
}
