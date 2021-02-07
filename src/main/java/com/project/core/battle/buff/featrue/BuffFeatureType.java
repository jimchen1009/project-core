package com.project.core.battle.buff.featrue;

import com.game.common.util.IEnumBase;

public enum BuffFeatureType implements IEnumBase {
	None,
	Dizzy,
	Invincible,
	DamageLink,
	Attribute,
	Shield,
	Heal,
	Poison,
	;

	public int getId() {
		return ordinal();
	}
}
