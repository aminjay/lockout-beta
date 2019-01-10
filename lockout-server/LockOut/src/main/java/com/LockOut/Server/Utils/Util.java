package com.LockOut.Server.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.Iterator;

// Author: Josh
public class Util {
	public static List<String> parser(String str, String DEL)
	{
		String pattern = "\\s*" + DEL + "\\s*";
		ArrayList<String> strList = new ArrayList<String>();
		if (str.equals(""))
		{
			return strList;
		}
		Scanner s = new Scanner(str).useDelimiter(pattern);
		while (s.hasNext())
		{
			strList.add(s.next());
		}
		s.close();
		return strList;
	}
	
	public static String listToString(Iterable<String> list, String DEL)
	{
		String toReturn = "";
		Iterator<String> I = list.iterator();
		while (I.hasNext())
		{
			toReturn = toReturn + DEL + I.next();
		}
		return toReturn;
	}
	
	public static String rmString(String str, String DEL, String rmName)
	{
		List<String> tempList = parser(str, DEL);
		if (!tempList.remove(rmName))
		{
			return str;
		}
		return listToString(tempList, DEL);
	}
	
}
