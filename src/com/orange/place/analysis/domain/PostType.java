package com.orange.place.analysis.domain;

public enum PostType {
	CREATE(1), FORWARD(2), REPLY(3);

	private int value;

	public int getValue() {
		return value;
	}

	PostType(int value) {
		this.value = value;
	}

	public static PostType getType(int value) {
		PostType result = PostType.CREATE;
		PostType[] types = PostType.values();
		for (PostType t : types) {
			if (t.getValue() == value) {
				result = t;
				break;
			}
		}
		return result;
	}
}
