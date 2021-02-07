package com.project.core.battle.buff;

import com.project.core.battle.BattleTeamType;
import com.project.core.battle.BattleUnit;
import com.project.core.battle.BattleXXX;
import com.project.core.battle.buff.dec.BuffDecPoint;
import com.project.core.battle.buff.featrue.IBuffFeature;
import com.project.core.battle.model.BattleBuffData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Buff extends BattleXXX<BattleBuffData> {

	private static final Logger logger = LoggerFactory.getLogger(Buff.class);

	private final long id;
	private final int initRound;
	private final BattleUnit casterUnit;
	private final List<IBuffFeature> featureList;

	private BuffCasterType casterType;
	private BattleUnit hostUnit;

	private final BuffConfig buffConfig;
	private final BuffTypeConfig typeConfig;

	public Buff(long id, BattleBuffData data, BattleUnit casterUnit) {
		super(data);
		this.id = id;
		this.initRound = data.getRound();
		this.casterUnit = casterUnit;

		this.casterType = BuffCasterType.UnKnown;
		this.hostUnit = null;

		this.buffConfig = BuffConfig.getConfig(data.getBuffId());
		this.typeConfig = BuffTypeConfig.getConfig(buffConfig.getType());

		this.featureList = buffConfig.createFeatureList(this);
	}

	public long getId() {
		return id;
	}

	public int getBuffId() {
		return getData().getBuffId();
	}

	public int getType() {
		return buffConfig.getType();
	}

	public BuffCasterType getCasterType() {
		return casterType;
	}

	public BattleUnit getCasterUnit() {
		return casterUnit;
	}

	public int getInitRound() {
		return initRound;
	}

	public int getRemainRound() {
		return getData().getRound();
	}

	public boolean isRoundUsedUp() {
		return !isRemainRound();
	}

	public boolean isRemainRound() {
		return getRemainRound() > 0;
	}

	public void setRemainRound(int round){
		getData().setRound(round);
	}

	public int decRemainRound(int decValue) {
		if (decValue <= 0){
			return 0;
		}
		int oldRemainRound = getRemainRound();
		setRemainRound(Math.max(0, oldRemainRound - decValue));
		return oldRemainRound - getRemainRound();
	}

	public int addRemainRound(int addValue) {
		if (addValue <= 0){
			return 0;
		}
		int oldRemainRound = getRemainRound();
		int remainRound = oldRemainRound + addValue;
		if (buffConfig.getMaxRound() > 0 && remainRound > buffConfig.getMaxRound()) {
			remainRound = buffConfig.getMaxRound();
		}
		setRemainRound(remainRound);
		return remainRound - oldRemainRound;
	}

	public void setHostUnit(BattleUnit hostUnit) {
		if (this.hostUnit != null) {
			return;
		}
		if (casterUnit != null) {
			if (casterUnit.getId() == hostUnit.getId()) {
				this.casterType = BuffCasterType.Own;
			}
			else if (casterUnit.getTeamType().equals(hostUnit.getTeamType())) {
				this.casterType = BuffCasterType.TeamMate;
			}
			else if (casterUnit.getTeamType().equals(BattleTeamType.Neutral)){
				this.casterType = BuffCasterType.Neutral;
			}
			else {
				this.casterType = BuffCasterType.Enemy;
			}
		}
		this.hostUnit = hostUnit;
	}

	public BattleUnit getHostUnit() {
		return hostUnit;
	}

	public List<IBuffFeature> getFeatureList() {
		return featureList;
	}

	public BuffConfig getBuffConfig() {
		return buffConfig;
	}

	public BuffTypeConfig getTypeConfig() {
		return typeConfig;
	}

	public int buffGenera(){
		return buffConfig.buffGenera();
	}

	public BuffDecPoint getBuffDecPoint(){
		return typeConfig.getBuffDecPoint(casterType);
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", initRound=" + initRound +
				", casterUnit=" + casterUnit.getId() +
				", casterType=" + casterType +
				", hostUnit=" + hostUnit +
				", buffConfig=" + buffConfig +
				", typeConfig=" + typeConfig +
				'}';
	}
}
