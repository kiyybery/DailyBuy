package izhipeng.dailybuy.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: LiuYu
 * Date: 13-1-15
 * Time: 下午12:19
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    /**
     * 判断字符窜是否不为null和空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }
    public static String getDownloadFileName(String audio_url){//获取下载的音频文件名�? 	
    	int lastPosition = audio_url.lastIndexOf('/');
    	String audio_name = audio_url.substring(lastPosition, audio_url.length());
    	return audio_name;
    }
    public static String join(String join,String[] strAry){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<strAry.length;i++){
             if(i==(strAry.length-1)){
                 sb.append(strAry[i]);
             }else{
                 sb.append(strAry[i]).append(join);
             }
        }
       
        return new String(sb);
    }

    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^1\\d{10}$");
//        Pattern p = Pattern.compile("^((\\+86)|(86))?(13)\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
