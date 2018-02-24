package GameObject;

import Factory.Constants;
import Map.*;
import java.util.ArrayList;

/**
 * Created by msi1 on 1/21/2018.
 */
public class ArcherTower extends Tower
{
    public ArcherTower(int id, int x, int y, int lv)
    {
        super(id, x, y, lv, (int) Constants.TOWERS_CONSTANTS[0][3], Constants.TOWERS_CONSTANTS[0][4],
                (int) Constants.TOWERS_CONSTANTS[0][5], (int) Constants.TOWERS_CONSTANTS[0][6]);
    }

//    public ArrayList<Unit> locateTarget(Map map)
//    {
//        ArrayList<Unit> targets = firstTargetFinder(map);
//        if (targets.size() > 1)
//        {
//            targets = secondTargetFinder(targets);
//        }
//
//        if (targets.size() > 0)
//        {
//            return targets.get(0);
//        }
//
//        return null;
//    }

    @Override
    public ArrayList<Unit> fire(Unit unit)
    {
        ArrayList<Unit> casualties = new ArrayList<>();
        if (tickPerAttack == attackCounter)
        {
            unit.setHealth(unit.getHealth() - (int) ((double) getDamage()*Math.pow(getDamageCoeff(), getLevel() - 1)));
            if (unit.getHealth() <= 0)
            {
                casualties.add(unit);
            }

        }

        if (--attackCounter == 0)
        {
            attackCounter = tickPerAttack;
        }
        return casualties;
    }

    @Override
    protected ArrayList<Unit> secondTargetFinder(ArrayList<Unit> targets)
    {
        targets = checkUnitTypes(targets);
        if (targets.size() > 0)
        {
            targets = checkUnitHealths(targets);
        }

        return targets;
    }

    private ArrayList<Unit> checkUnitHealths(ArrayList<Unit> targets)
    {
        ArrayList<Unit> finalTargets = new ArrayList<>();
        int minHealth = Integer.MAX_VALUE;

        for (Unit target : targets)
        {
            if (target.getHealth() < minHealth)
            {
                minHealth = target.getHealth();
                finalTargets.clear();
                finalTargets.add(target);
            }
        }

        return finalTargets;
    }

    private ArrayList<Unit> checkUnitTypes(ArrayList<Unit> targets)
    {
        ArrayList<Unit> heavyUnits = new ArrayList<>();

        for (Unit target : targets)
        {
            if (target instanceof HeavyUnit)
            {
                heavyUnits.add(target);
            }
        }

        if (heavyUnits.size() != 0)
        {
            return heavyUnits;
        }
        return targets;
    }
}
