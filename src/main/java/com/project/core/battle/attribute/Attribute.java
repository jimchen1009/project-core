package com.project.core.battle.attribute;

import com.game.common.util.IEnumBase;

public enum Attribute implements IEnumBase {
	hp( AttributeType.Both, "血量"),
	thp( AttributeType.Both,"总血量"),
	atk( AttributeType.Attack,"攻击"),
	def(AttributeType.Defence, "防御"),
	critic( AttributeType.Attack, "暴击"),
	anti_critic( AttributeType.Defence, "抗暴"),
	critic_dmg( AttributeType.Attack, "暴击伤害"),
	hit( AttributeType.Attack, "命中"),
	dodge( AttributeType.Defence, "闪避"),
	atk_damage_add( AttributeType.Attack, "攻击伤害增加（千分比）"),
	def_damage_dec( AttributeType.Defence, "防御伤害减免（千分比，负数为伤害加深）"),
	effect_hit( AttributeType.Attack, "效果命中"),
	effect_resist( AttributeType.Defence, "效果抵抗"),
	;

	private final AttributeType type;

	Attribute(AttributeType type, String comment) {
		this.type = type;
	}

	public AttributeType getType() {
		return type;
	}

	@Override
	public int getId() {
		return ordinal();
	}
}
