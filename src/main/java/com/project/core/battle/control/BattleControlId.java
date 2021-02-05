package com.project.core.battle.control;

import java.util.Objects;

public class BattleControlId{

	private final String name;

	public BattleControlId(String name) {
		this.name = name;
	}


	public BattleControlId createChild(String childName){
		return new BattleControlId(String.format("%s.%s", this.name, childName));
	}

	public String getName() {
		return name;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BattleControlId that = (BattleControlId) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return name;
	}
}
