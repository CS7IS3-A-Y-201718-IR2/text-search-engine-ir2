package com.ir.searchengine.utils;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordFrequency {

	public Map<String, Integer> getFrequency(String line) {
		try (Scanner input = new Scanner(line)) {
			Map<String, Integer> wordCounts = new TreeMap<String, Integer>();
			while (input.hasNext()) {
				String next = input.next().toLowerCase();
				if (!wordCounts.containsKey(next)) {
					wordCounts.put(next, 1);
				} else {
					wordCounts.put(next, wordCounts.get(next) + 1);
				}
			}
			
			return wordCounts;
		}
	}
}
