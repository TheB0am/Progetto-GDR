package org.it.unicam.cs.mpgc.rpg125943;

import java.util.List;

public class GameSaveData {
    
    private PlayerData player;
    private List<EnemyData> enemies;
    private int enemyIndex;
    private boolean fightingBoss;
    private EnemyData boss;
    
    
    public GameSaveData(){
    }
    
    public GameSaveData(PlayerData player, List<EnemyData> enemies, int enemyIndex, boolean fightBoss, EnemyData boss) {
        this.player = player;
        this.enemies = enemies;
        this.enemyIndex = enemyIndex;
        this.fightingBoss = fightBoss;
        this.boss = boss;
    }
    
    public PlayerData getPlayer() {
        return player;
    }
    
    public List<EnemyData> getEnemies() {
        return enemies;
    }
    
    public int getEnemyIndex() {
        return enemyIndex;
    }
    
    public boolean isFightBoss() {
        return fightingBoss;
    }
    
    public EnemyData getBoss() {
        return boss;
    }
    
}