package pa1;


// You are NOT required to use or understand this class
public class PrintUtils {
	public static String allignMid(String a, int len)
	{
		int space=len-a.length();
		if (space<=1) {
			return String.format("%-"+len+"."+len+"s",a);
		}
		return String.format("%-"+space/2+"s%-"+len+"."+len+"s%-"+space/2+"s", "",a,"");
	}
}
