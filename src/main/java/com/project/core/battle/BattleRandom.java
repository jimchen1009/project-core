package com.project.core.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleRandom {

	private Random random;

	public BattleRandom(long seed) {
		this.random = new Random(seed);
	}

	public int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public <T> T select(List<T> randomTList) {
		if (randomTList == null || randomTList.isEmpty()) {
			return null;
		}
		int index = nextInt(randomTList.size());
		return randomTList.get(index);
	}

	public boolean hit(int rate, final int base) {
		return random.nextInt(base) < rate;
	}

	public float random(float begin, float end) {
		if (begin >= end) {
			return begin;
		}
		else {
			return random.nextFloat() * (end - begin) + begin;
		}
	}

	public int random(int begin, int end) {
		if (begin >= end) {
			return begin;
		}
		else {
			return random.nextInt(end - begin) + begin;
		}
	}

	public <T> List<T> select(List<T> srcList, int count){
		if (srcList.size() <= count) {
			return srcList;
		}
		List<T> arrayList = new ArrayList<>(srcList);
		shuffle(arrayList, count);
		return arrayList.subList(0, count);
	}

	/**
	 * 把一个数组的前 n 位打乱
	 * @param srcList 原始数组, 执行完后前 n 位是随机得到的值, 后 (长度-n) 位是其余的值
	 * @param count 指定前 n 位
	 */
	public <T> void shuffle(List<T> srcList, int count) {
		int len = srcList.size();
		for (int i = 0; count > i; i++) {
			int swapIndex = random(i, len);
			if (i != swapIndex) {
				T swap = srcList.get(swapIndex);
				srcList.set(swapIndex, srcList.get(i));
				srcList.set(i, swap);
			}
		}
	}
}
