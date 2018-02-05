import GameObject.Tower;
import GameObject.Unit;
import GameObject.WarObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by msi1 on 1/31/2018.
 */
public class TurnEvents
{
    private ArrayList<Pair<Integer, Unit>> turnNewUnits = new ArrayList<>();
    private ArrayList<Pair<Integer, Tower>> turnNewTowers = new ArrayList<>();
    private ArrayList<Pair<Integer, Tower>> turnNewPartialTowers = new ArrayList<>();

    private ArrayList<Pair<Integer, Tower>> turnTowerUpdates = new ArrayList<>();
    private ArrayList<Pair<Integer, Tower>> turnPartialTowerUpdates = new ArrayList<>();

    private ArrayList<Pair<Integer, WarObject>> turnUnitCasualties = new ArrayList<>();
    private ArrayList<Pair<Integer, WarObject>> turnTowerCasualties = new ArrayList<>();

    private ArrayList<Pair<Integer, Point>> turnNukeLocations = new ArrayList<>();
    private ArrayList<Pair<Integer, Point>> turnBeanLocations = new ArrayList<>();

    private ArrayList<Pair<Tower, Unit>> turnArcherAttacks = new ArrayList<>();
    private ArrayList<Pair<Tower, Unit>> turnCannonAttacks = new ArrayList<>();

    private ArrayList<Pair<Integer, WarObject>> turnSuccessfulUnits = new ArrayList<>();

    private Gson gson = new Gson();
    private JsonArray jsonArray;

    public void addNewUnit(int playerId, Unit unit)
    {
        turnNewUnits.add(new Pair<>(1 - playerId, unit));
    }

    public void addNewTower(int playerId, Tower tower)
    {
        turnNewTowers.add(new Pair<>(playerId, tower));
    }

    public void addNewPartialTower(int playerId, Tower tower)
    {
        turnNewPartialTowers.add(new Pair<>(playerId, tower));
    }

    public void addNewNukeLocation(int playerId, int[] nukeLocation)
    {
        Point point = new Point(nukeLocation[0], nukeLocation[1]);
        turnNukeLocations.add(new Pair<>(playerId, point));
    }

    public void addNewBeanLocation(int playerId, int[] beanLocation)
    {
        Point point = new Point(beanLocation[0], beanLocation[1]);
        turnBeanLocations.add(new Pair<>(1 - playerId, point));
    }

    public void addUnitCasualties(int playerId, Collection<Unit> casualties)
    {
        for (Unit unit : casualties)
        {
            turnUnitCasualties.add(new Pair<>(1 - playerId, unit));
        }
    }

    public void addTowerCasualties(int playerId, Tower tower)
    {
        turnTowerCasualties.add(new Pair<>(playerId, tower));
    }

    public void addNewTowerUpdate(int playerId, Tower tower)
    {
        turnTowerUpdates.add(new Pair<>(playerId, tower));
    }

    public void addNewPartialTowerUpdate(int playerId, Tower tower)
    {
        turnPartialTowerUpdates.add(new Pair<>(playerId, tower));
    }

    public void addSuccessfulUnit(int mapId, Unit unit)
    {
        turnSuccessfulUnits.add(new Pair<>(mapId, unit));
    }

    public void addArcherAttack(Tower tower, Unit unit)
    {
        turnArcherAttacks.add(new Pair<>(tower, unit));
    }

    public void addCannonAttack(Tower tower, Unit unit)
    {
        turnCannonAttacks.add(new Pair<>(tower, unit));
    }

    public JsonArray getNewUnitsJson()
    {
        Object[][] unitsData = new Object[turnNewUnits.size()][8];
        for (int i = 0; i < turnNewUnits.size(); i++)
        {
            Unit unit = turnNewUnits.get(i).getValue();
            unitsData[i] = new Object[]{turnNewUnits.get(i).getKey(), unit.getData()};
        }

        jsonArray = (JsonArray) gson.toJsonTree(unitsData);
        return jsonArray;
    }

    public JsonArray getUpdTowersJson()
    {
        Object[][] updatedTowersData = new Object[turnTowerUpdates.size() + turnNewTowers.size()][4];
        for (int i = 0; i < turnTowerUpdates.size(); i++)
        {
            Tower tower = turnTowerUpdates.get(i).getValue();
            updatedTowersData[i] = new Object[]{turnTowerUpdates.get(i).getKey(), tower.getId(), tower.getLevel(), false};
        }
        for (int i = 0; i < turnNewTowers.size(); i++)
        {
            Tower tower = turnNewTowers.get(i).getValue();
            updatedTowersData[i] = new Object[]{turnNewTowers.get(i).getKey(), tower.getId(), tower.getLevel(), true};
        }

        jsonArray = (JsonArray) gson.toJsonTree(updatedTowersData);
        return jsonArray;
    }

    public JsonArray getUpdTowersPartialJson()
    {
        Object[][] updatedTowersData = new Object[turnPartialTowerUpdates.size() + turnNewPartialTowers.size()][4];

        for (int i = 0; i < turnPartialTowerUpdates.size(); i++)
        {
            Tower tower = turnPartialTowerUpdates.get(i).getValue();
            updatedTowersData[i] = new Object[]{turnPartialTowerUpdates.get(i).getKey(), tower.getId(), tower.getLevel(), false};
        }
        for (int i = 0; i < turnNewPartialTowers.size(); i++)
        {
            Tower tower = turnNewPartialTowers.get(i).getValue();
            updatedTowersData[i] = new Object[]{turnNewPartialTowers.get(i).getKey(), tower.getId(), tower.getLevel(), true};
        }

        jsonArray = (JsonArray) gson.toJsonTree(updatedTowersData);
        return jsonArray;
    }

    public JsonArray getDeadUnitsJson(int id, boolean isForUI)
    {
        Object[][] deadUnitsData = new Object[turnUnitCasualties.size()][2];
        for (int i = 0; i < turnUnitCasualties.size(); i++)
        {
            WarObject unit = turnUnitCasualties.get(i).getValue();
            if (isForUI)
            {
                deadUnitsData[i] = new Object[]{turnUnitCasualties.get(i).getKey(), unit.getId()};
            } else
            {
                deadUnitsData[i] = new Object[]{turnUnitCasualties.get(i).getKey() ^ id, unit.getId()};
            }
        }
        jsonArray = (JsonArray) gson.toJsonTree(deadUnitsData);
        return jsonArray;
    }

    public JsonArray getSuccessfulUnitsJson(int id, boolean isForUI)
    {
        Object[][] successfulUnitsData = new Object[turnSuccessfulUnits.size()][2];
        for (int i = 0; i < turnSuccessfulUnits.size(); i++)
        {
            WarObject unit = turnSuccessfulUnits.get(i).getValue();
            if (isForUI)
            {
                successfulUnitsData[i] = new Object[]{turnSuccessfulUnits.get(i).getKey(), unit.getId()};
            } else
            {
                successfulUnitsData[i] = new Object[]{turnSuccessfulUnits.get(i).getKey() ^ id, unit.getId()};
            }
        }
        jsonArray = (JsonArray) gson.toJsonTree(successfulUnitsData);
        return jsonArray;
    }

    public JsonArray getTowerCasualtiesJson(int id, boolean isForUI)
    {
        Object[][] towerCasualtiesData = new Object[turnTowerCasualties.size()][2];
        for (int i = 0; i < turnTowerCasualties.size(); i++)
        {
            WarObject unit = turnTowerCasualties.get(i).getValue();
            if (isForUI)
            {
                towerCasualtiesData[i] = new Object[]{turnTowerCasualties.get(i).getKey(), unit.getId()};
            } else
            {
                towerCasualtiesData[i] = new Object[]{turnTowerCasualties.get(i).getKey() ^ id, unit.getId()};
            }
        }
        jsonArray = (JsonArray) gson.toJsonTree(towerCasualtiesData);
        return jsonArray;
    }

    public JsonArray getBeansJson(int id, boolean isForUI)
    {
        Object[][] beansData = new Object[turnBeanLocations.size()][2];
        for (int i = 0; i < turnBeanLocations.size(); i++)
        {
            Point location = turnBeanLocations.get(i).getValue();
            if (isForUI)
            {
                beansData[i] = new Object[]{turnBeanLocations.get(i).getKey(), location};
            } else
            {
                beansData[i] = new Object[]{turnBeanLocations.get(i).getKey() ^ id, location};
            }
        }
        jsonArray = (JsonArray) gson.toJsonTree(beansData);
        return jsonArray;
    }

    public JsonArray getNukesJson(int id, boolean isForUI)
    {
        Object[][] nukesData = new Object[turnNukeLocations.size()][2];
        for (int i = 0; i < turnNukeLocations.size(); i++)
        {
            Point location = turnNukeLocations.get(i).getValue();
            if (isForUI)
            {
                nukesData[i] = new Object[]{turnNukeLocations.get(i).getKey(), location};
            } else
            {
                nukesData[i] = new Object[]{turnNukeLocations.get(i).getKey() ^ id, location};
            }
        }
        jsonArray = (JsonArray) gson.toJsonTree(nukesData);
        return jsonArray;
    }

    public JsonArray getArcherAttacksJson()
    {
        Object[][] archerAttacksData = new Object[turnArcherAttacks.size()][2];
        for (int i = 0; i < turnArcherAttacks.size(); i++)
        {
            Tower tower = turnArcherAttacks.get(i).getKey();
            Unit unit = turnArcherAttacks.get(i).getValue();
            archerAttacksData[i] = new Object[]{tower.getId(), unit.getId()};
        }
        jsonArray = (JsonArray) gson.toJsonTree(archerAttacksData);
        return jsonArray;
    }

    public JsonArray getCannonAttacksJson()
    {
        Object[][] cannonAttacksData = new Object[turnCannonAttacks.size()][2];
        for (int i = 0; i < turnCannonAttacks.size(); i++)
        {
            Tower tower = turnCannonAttacks.get(i).getKey();
            Unit unit = turnCannonAttacks.get(i).getValue();
            Point point = new Point(unit.getPath().getCells().get(unit.getCurrentCellIndex()).getX()
                    , unit.getPath().getCells().get(unit.getCurrentCellIndex()).getY());
            cannonAttacksData[i] = new Object[]{tower.getId(), point};
        }
        jsonArray = (JsonArray) gson.toJsonTree(cannonAttacksData);
        return jsonArray;
    }

    public void clear()
    {
        turnNewUnits = new ArrayList<>();
        turnNewTowers = new ArrayList<>();
        turnNewPartialTowers = new ArrayList<>();
        turnTowerUpdates = new ArrayList<>();
        turnPartialTowerUpdates = new ArrayList<>();

        turnUnitCasualties = new ArrayList<>();
        turnTowerCasualties = new ArrayList<>();

        turnNukeLocations = new ArrayList<>();
        turnBeanLocations = new ArrayList<>();

        turnArcherAttacks = new ArrayList<>();
        turnCannonAttacks = new ArrayList<>();

        turnSuccessfulUnits = new ArrayList<>();
    }
}
