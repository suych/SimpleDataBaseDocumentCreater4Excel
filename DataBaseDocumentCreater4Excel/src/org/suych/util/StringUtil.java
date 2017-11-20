package org.suych.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	public static String null2Empty(Object input) {
		if (input == null || StringUtils.isBlank(input.toString()) || "null".equals(input.toString())) {
			return "";
		} else {
			return input.toString();
		}
	}

}
