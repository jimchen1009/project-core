package com.project.core.battle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleMath {

	private static final Logger logger = LoggerFactory.getLogger(BattleMath.class);

	public static int rate1000(int value, int rate){
		long longValue = ((long) value * rate) / 1000;
		if (longValue < Integer.MAX_VALUE) {
			return (int)longValue;
		}
		logger.error("数值溢出, value:{} rate:{} longValue:{}", value, rate, longValue);
		return Integer.MAX_VALUE;
	}

	public static int addExact(int ... addValues){
		int totalValue = 0;
		try {
			for (int addValue : addValues) {
				totalValue = Math.addExact(totalValue, addValue);
			}
			return totalValue;
		}
		catch (Throwable t){
			logger.error("数值溢出, addValues:{} ", addValues);
			return Integer.MAX_VALUE;
		}
	}
}
