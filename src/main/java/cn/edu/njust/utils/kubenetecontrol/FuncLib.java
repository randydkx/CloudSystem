package cn.edu.njust.utils.kubenetecontrol;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.GZIPInputStream;

public class FuncLib {

	
	/**
	 * Sample a date between the given duration
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public static Date SampleBetween(Date fromdate, Date todate)
	{
		long duration=todate.getTime()-fromdate.getTime();
		
		Random rm=new Random();
		long newdistance=(long) (rm.nextDouble()*duration);
		Date sampleddate=new Date();
		sampleddate.setTime(fromdate.getTime()+newdistance);
		return sampleddate;
		
	}
	public static boolean EqualDouble(double a,double b,int digital)
	{
		double gap=a-b;
		
		if(Math.abs(gap)>Math.pow(10, digital))
		{
			return false;
		}
		else
			return true;
	}
	public static boolean EqualLessDouble(double a,double b,int digital)
	{
		if(a<b)
			return true;
		double gap=a-b;
		
		if(Math.abs(gap)>Math.pow(10, digital))
		{
			return false;
		}
		else
			return true;
	}
	public static boolean LessDouble(double a,double b,int digital)
	{
		double gap=a-b;
		if(Math.abs(gap)>Math.pow(10, digital))
		{
			if(a<b)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
	/**
	 * Check whether the two date are within same minute
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean isSameMinute(Date startDate, Date endDate)
	{
		String str1=sdf.format(startDate);
		String str2=sdf.format(endDate);
		if(str1.substring(0, 15).equalsIgnoreCase(str2.substring(0, 15)))
			return true;
		else
			return false;
		
		/* Calendar cal1 = Calendar.getInstance();
	     cal1.setTime(startDate);

	     Calendar cal2 = Calendar.getInstance();
	     cal2.setTime(endDate);

	     boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
	               .get(Calendar.YEAR);
	     boolean isSameMonth = isSameYear
	               && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
	     boolean isSameDate = isSameMonth
	               && cal1.get(Calendar.DAY_OF_MONTH) == cal2
	                       .get(Calendar.DAY_OF_MONTH);
	     boolean isSameHour = isSameDate
	               && cal1.get(Calendar.HOUR_OF_DAY) == cal2
	                       .get(Calendar.HOUR_OF_DAY);
	     boolean isSameMinute = isSameHour
	               && cal1.get(Calendar.MINUTE) == cal2
	                       .get(Calendar.MINUTE);
	    return isSameMinute;*/
	}
	/**
	 * unzip gzfile
	 * @param gzfilepath
	 * @param destpath
	 * @return
	 */
	public static boolean Ungz(String gzfilepath)
	{
	        String ouputfile = "";
	        try {  
	            //建立gzip压缩文件输入�? 
	            FileInputStream fin = new FileInputStream(gzfilepath);   
	            //建立gzip解压工作�?
	            GZIPInputStream gzin = new GZIPInputStream(fin);   
	            //建立解压文件输出�?  
	            ouputfile = gzfilepath.substring(0,gzfilepath.lastIndexOf('.'));
	            
	            FileOutputStream fout = new FileOutputStream(ouputfile);   
	            
	            int num;
	            byte[] buf=new byte[1024];

	            while ((num = gzin.read(buf,0,buf.length)) != -1)
	            {   
	                fout.write(buf,0,num);   
	            }

	            gzin.close();   
	            fout.close();   
	            fin.close();   
	        } catch (Exception ex){  
	            System.err.println(ex.toString());  
	            return false;
	        }  
	        return true;
	}
}
