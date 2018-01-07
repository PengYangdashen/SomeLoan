package Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static void paseMill(String dateTime) {
		// TODO Auto-generated method stub
		/**
		 * 灏嗗瓧绗︿覆鏁版嵁杞寲涓烘绉掓暟
		 */
//		String dateTime="20121025112950";
		   Calendar c = Calendar.getInstance();
		  
		try {
			c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(dateTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("时间转化后的毫秒数为："+c.getTimeInMillis());
	}
	
	public static String parseDate(Long time) {
		// TODO Auto-generated method stub
		/**
		* 灏嗘绉掓暟杞寲涓烘椂闂?
		*/
		  DateFormat formatter = new SimpleDateFormat("yyyy骞碝M鏈坉d鏃? HH:mm");
		
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTimeInMillis(time);
		
		return formatter.format(calendar.getTime());
	}
	
	public static String parseDate2(Long time) {
		// TODO Auto-generated method stub
		/**
		* 灏嗘绉掓暟杞寲涓烘椂闂?
		*/
		  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTimeInMillis(time);
		
		return formatter.format(calendar.getTime());
	}
	
	public static String parseDate3(Long time) {
		// TODO Auto-generated method stub
		/**
		* 灏嗘绉掓暟杞寲涓烘椂闂?
		*/
		  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTimeInMillis(time);
		
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * 锟斤拷取系统时锟斤拷(去锟斤拷锟斤拷锟斤拷)
	 * 
	 * @return
	 */
	public static String getDate() {
		Date date = new Date();
		Long time = date.getTime();
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String r=sdf.format(d);
		return r.substring(0, r.lastIndexOf(":"));
	}
	/**
	 * 锟斤拷取锟斤拷锟斤拷锟斤拷
	 * @return
	 */
	public static String getYMD(){
		String data=getDate();
		return data.split(" ")[0];
	}
	/**
	 * 锟斤拷取系统时锟斤拷
	 * 
	 * @return
	 */
	public static String getDate2() {
		Date date = new Date();
		Long time = date.getTime();
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	/**
	 * 锟斤拷取系统时锟斤拷锟?锟斤拷锟?
	 * 
	 * @return
	 */
	public static String getYear() {
		String time = getDate2();
		String[] str = time.split("-");
		return str[0];
	}

	/**
	 * 锟斤拷取系统时锟斤拷锟?锟铰凤拷
	 * 
	 * @return
	 */
	public static String getMonth() {
		String time = getDate2();
		String[] str = time.split("-");
		return str[1];
	}

	/**
	 * 锟斤拷取系统时锟斤拷锟?锟斤拷锟斤拷
	 * 
	 * @return
	 */
	public static String getDay() {
		String time = getDate2();
		String[] str = time.split("-");

		return str[2].substring(0, 2);
	}

	/**
	 * 锟斤拷取系统时锟斤拷锟?小时
	 * 
	 * @return
	 */
	public static String getHour() {

		String time = getDate2();
		String[] str = time.split(" ");

		return str[1].substring(0, 2);
	}

	/**
	 * 锟斤拷取系统时锟斤拷锟?锟斤拷锟斤拷
	 * 
	 * @return
	 */
	public static String getMinute() {

		String time = getDate2();
		String[] str = time.split(" ");

		return str[1].substring(3, 5);
	}

	/**
	 * 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷
	 * 
	 * @return
	 */
	public static String getMaxSendTime(String date, String hourAndMinute) {
		String result=null;
		// 锟斤拷date锟斤拷锟斤拷一锟斤拷默锟斤拷时锟戒，锟斤拷止锟斤拷锟阶拷锟斤拷斐?
		if (date.length() != 10) {
			date = "2015-07-01";
		}
		String time = date + " " + hourAndMinute+":00";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Date dt2 = sdf.parse(time);
			long haomiao = dt2.getTime();
			haomiao = haomiao + 30 * 60 * 1000;
			Date d = new Date(haomiao);
			time = sdf.format(d);
			String[] array = time.split(" ");
			result= array[1].substring(0, 5);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
