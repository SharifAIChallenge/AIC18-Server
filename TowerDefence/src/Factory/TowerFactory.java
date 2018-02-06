package Factory;

import GameObject.ArcherTower;
import GameObject.CannonTower;
import GameObject.Tower;

/**
 * Created by msi1 on 1/22/2018.
 */
public class TowerFactory {
    private static int nextId = 1;
    private int leftoverMoney;
    private int archerTowerCost;
    private int archerTowerLevelUpInitCost;
    private double archerTowerLevelUpCostCoeff;
    private int cannonTowerCost;
    private int cannonTowerLevelUpInitCost;
    private double cannonTowerLevelUpCostCoeff;

    public TowerFactory() {
        this.archerTowerCost = (int) Constants.TOWERS_CONSTANTS[0][0];
        this.archerTowerLevelUpInitCost = (int) Constants.TOWERS_CONSTANTS[0][1];
        this.archerTowerLevelUpCostCoeff = Constants.TOWERS_CONSTANTS[0][2];

        this.cannonTowerCost = (int) Constants.TOWERS_CONSTANTS[1][0];
        this.cannonTowerLevelUpInitCost = (int) Constants.TOWERS_CONSTANTS[1][1];
        this.cannonTowerLevelUpCostCoeff = Constants.TOWERS_CONSTANTS[1][2];
    }

    public static Tower createCopy(Tower tower) {
        if (tower instanceof ArcherTower) {
            return new ArcherTower(tower.getId(), tower.getX(), tower.getY(), tower.getLevel());
        }
        return new CannonTower(tower.getId(), tower.getX(), tower.getY(), tower.getLevel());
    }

    public static boolean isSameType(Tower firstTower, Tower secondTower) {
        return (firstTower instanceof ArcherTower && secondTower instanceof ArcherTower) ||
                (firstTower instanceof CannonTower && secondTower instanceof CannonTower);
    }

    public Tower createTower(Character type, int x, int y, int money) {
        if (type == 'a') {
            int cost = archerTowerCost;

            if (money - cost >= 0) {
                leftoverMoney = money - cost;
                return new ArcherTower(nextId++, x, y, 1);
            }
        } else if (type == 'c') {
            int cost = cannonTowerCost;

            if (money - cost >= 0) {
                leftoverMoney = money - cost;
                return new CannonTower(nextId++, x, y, 1);
            }
        }

        return null;
    }

    private int calculateArcherLevelUpCost(int lv) {
        if (lv == 1) {
            return 0;
        }

        return (int) ((double) archerTowerLevelUpInitCost * Math.pow(archerTowerLevelUpCostCoeff, (lv - 2)));
    }

    private int calculateCannonLevelUpCost(int lv) {
        if (lv == 1) {
            return 0;
        }

        return (int) ((double) cannonTowerLevelUpInitCost * Math.pow(cannonTowerLevelUpCostCoeff, (lv - 2)));
    }

    public int getLeftoverMoney() {
        return leftoverMoney;
    }

    public int getArcherTowerCost() {
        return archerTowerCost;
    }

    public void setArcherTowerCost(int archerTowerCost) {
        this.archerTowerCost = archerTowerCost;
    }

    public int getCannonTowerCost() {
        return cannonTowerCost;
    }

    public void setCannonTowerCost(int cannonTowerCost) {
        this.cannonTowerCost = cannonTowerCost;
    }
}
