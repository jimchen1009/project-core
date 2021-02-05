package com.project.core.battle.attribute;

import com.game.common.expression.IExprParams;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * 使用Map 与 多个int对象成员 有何区别？
 */
public class BaseAttributes implements Serializable {

	private Map<Attribute, Integer> attributes;

	public BaseAttributes() {
		this(new EnumMap<>(Attribute.class));
	}

	public BaseAttributes(Map<Attribute, Integer> attributes) {
		this.attributes = attributes;
	}

	public int getHp() {
		return get(Attribute.hp);
	}

	public int get(Attribute attribute) {
		return attributes.getOrDefault(attribute, 0);
	}

	public void set(Attribute attribute, int value) {
		attributes.put(attribute, value);
	}

	public BaseAttributes add(BaseAttributes attributes){
		for (Attribute attribute : Attribute.values()) {
			int value = get(attribute);
			set(attribute, value + attributes.get(attribute));
		}
		return this;
	}

	public BaseAttributes copy() {
		BaseAttributes copy = new BaseAttributes();
		copy.add(this);
		return copy;
	}

	public int getHpRate() {
		return (int) ((long) get(Attribute.hp) * 1000 / get(Attribute.thp));
	}

	@Override
	public String toString() {
		return attributes.toString();
	}

	public IExprParams attributeParams(IExprParams params) {
		for (Attribute attribute : Attribute.values()) {
			params.put(attribute.name(), get(attribute));
		}
		return params;
	}
}
