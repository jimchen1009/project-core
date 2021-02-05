package com.project.core.battle.control.pve;

import com.project.core.battle.Battle;
import com.project.core.battle.BattleUnitType;
import com.project.core.battle.BattleWinLos;
import com.project.core.battle.attribute.AttributeConfig;
import com.project.core.battle.attribute.BaseAttributes;
import com.project.core.battle.control.BattleControlService;
import com.project.core.battle.control.BattleType;
import com.project.core.battle.model.BattleData;
import com.project.core.battle.model.BattleDataBuilder;
import com.project.core.battle.model.BattleSkillData;
import com.project.core.battle.model.BattleUnitData;
import com.project.core.config.data.DataConfigs;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleControlRunner {

	private static List<Integer> skillConfigIds0 = Arrays.asList(41200007, 41200008, 41200009);
	private static List<Integer> skillConfigIds1 = Arrays.asList(41200107, 41200108, 41200109);
	private static List<Integer> skillConfigIds3 = Arrays.asList(41200307, 41200308, 41200309);

	private static final List<TeamConfig> TeamAConfigList = Arrays.asList(
			new TeamConfig(10093, skillConfigIds0),
			new TeamConfig(10093, skillConfigIds1)
	);

	private static final List<TeamConfig> TeamBConfigList = Arrays.asList(
			new TeamConfig(11090, skillConfigIds3)
	);

	private static final int TeamUnit = Math.min(TeamAConfigList.size(), TeamBConfigList.size());

	@Test
	public void runPVEBattle(){
		System.setProperty("game.project.config.path", "D:/demo/project-core/src/main/resources/project_config.conf");
		DataConfigs.init();
		List<BattleUnitData> teamADataUnitList = new ArrayList<>();
		List<BattleUnitData> teamBDataUnitList = new ArrayList<>();
		for (int i = 0; i < TeamUnit; i++) {
			teamADataUnitList.add(createBattleUnit(1, i, TeamAConfigList.get(i)));
			teamBDataUnitList.add(createBattleUnit(2, i, TeamBConfigList.get(i)));
		}
		BattleData battleData = new BattleDataBuilder().addTeamUnitWithEnemy(teamADataUnitList, teamBDataUnitList, (teamUnitDataA, teamUnitDataB) -> {
			teamUnitDataA.setInitializeCd(true);
			teamUnitDataB.setInitializeCd(true);
		}).build("PVE", 0);
		battleData.setMaxRound(10000);
		Battle battle = BattleControlService.createBattle(BattleType.PVP, battleData);

		for (int i = 0; i < battleData.getMaxRound() * 10; i++) {
			BattleControlService.executeAI(1, battle);
			BattleControlService.executeAI(2, battle);
		}
		BattleWinLos battleWinLos = battle.getBattleWinLos();
		System.out.println("战斗回合: " + battleData.getCurRound() + ", 结果: " + battleWinLos);
	}

	private BattleUnitData createBattleUnit(long userId, int index, TeamConfig teamConfigA){
		AttributeConfig attributeConfig = AttributeConfig.getConfig(teamConfigA.attributeId);
		BaseAttributes attributes = attributeConfig.createAttributes();
		BattleUnitData battleUnitData = new BattleUnitData(userId, 0, BattleUnitType.Monster, index, 0, 0, 0, attributes);

		List<BattleSkillData> skillDataList = new ArrayList<>(teamConfigA.skillConfigIds.size());
		for (int i = 0; i < teamConfigA.skillConfigIds.size(); i++) {
			skillDataList.add(new BattleSkillData(i, teamConfigA.skillConfigIds.get(i), 0));
		}
		battleUnitData.setSkillDataList(skillDataList);
		return battleUnitData;
	}

	private static class TeamConfig {

		private final int attributeId;
		private final List<Integer> skillConfigIds;

		public TeamConfig(int attributeId, List<Integer> skillConfigIds) {
			this.attributeId = attributeId;
			this.skillConfigIds = skillConfigIds;
		}
	}
}
