package framework.gameObject;

import java.util.ArrayList;
import java.util.List;

import framework.math.bounds.Circle;
import framework.math.bounds.Rectangle;

public class SpatialHashGrid2D {

    private List<StaticObject2D>[] dynamicCells;
    private List<StaticObject2D>[] staticCells;
    private int cellsPerRow, cellsPerCol;
    private float cellSize;
    private int[] cellIds = new int[4];
    private List<StaticObject2D> foundObjects;


    @SuppressWarnings("unchecked")
    public SpatialHashGrid2D(float worldWidth, float worldHeight, float cellSize) {
        this.cellSize = cellSize;
        this.cellsPerRow = (int) Math.ceil(worldWidth / cellSize);
        this.cellsPerCol = (int) Math.ceil(worldHeight / cellSize);

        int numCells = cellsPerCol * cellsPerRow;
        dynamicCells = new List[numCells];
        staticCells = new List[numCells];

        for (int i = 0; i < numCells; i++) {
            dynamicCells[i] = new ArrayList<>(10);
            staticCells[i] = new ArrayList<>(10);
        }

        foundObjects = new ArrayList<>(10);
    }

    public void insertStaticObject(StaticObject2D obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;

        while (i <= 3 && (cellId = cellIds[i++]) != -1)
            staticCells[cellId].add(obj);
    }

    public void insertDynamicObject(StaticObject2D obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;

        while (i <= 3 && (cellId = cellIds[i++]) != -1)
            dynamicCells[cellId].add(obj);
    }

    public void removeObject(StaticObject2D obj) {
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            dynamicCells[cellId].remove(obj);
            staticCells[cellId].remove(obj);
        }
    }

    public void clearDynamicCells(StaticObject2D obj) {
        for (List<StaticObject2D> dynamicCell : dynamicCells)
            dynamicCell.clear();
    }

    public List<StaticObject2D> getPotentialColliders(StaticObject2D obj) {
        foundObjects.clear();
        int[] cellIds = getCellIds(obj);
        int i = 0;
        int cellId;

        while (i <= 3 && (cellId = cellIds[i++]) != -1) {
            int len = dynamicCells[cellId].size();

            for (int j = 0; j < len; j++) {
                StaticObject2D collider = dynamicCells[cellId].get(j);
                if (!foundObjects.contains(collider))
                    foundObjects.add(collider);
            }

            len = staticCells[cellId].size();
            for (int j = 0; j < len; j++) {
                StaticObject2D collider = staticCells[cellId].get(j);
                if (!foundObjects.contains(collider))
                    foundObjects.add(collider);
            }
        }
        return foundObjects;
    }

    public int[] getCellIds(StaticObject2D obj) {
        if (obj.bounds instanceof Rectangle) {
            int x1 = (int) Math.floor(((Rectangle) obj.bounds).lowerLeft.x / cellSize);
            int y1 = (int) Math.floor(((Rectangle) obj.bounds).lowerLeft.y / cellSize);

            int x2 = (int) Math.floor((((Rectangle) obj.bounds).lowerLeft.x +
                    ((Rectangle) obj.bounds).width) / cellSize);
            int y2 = (int) Math.floor((((Rectangle) obj.bounds).lowerLeft.y +
                    ((Rectangle) obj.bounds).height) / cellSize);

            if (x1 == x2 && y1 == y2) {
                if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                    cellIds[0] = x1 + y1 * cellsPerRow;
                } else
                    cellIds[0] = -1;
                cellIds[1] = -1;
                cellIds[2] = -1;
                cellIds[3] = -1;

            }
            else if (x1 == x2) {
                int i = 0;
                if (x1 >= 0 && x1 < cellsPerRow) {
                    if (y1 >= 0 && y1 < cellsPerCol)
                        cellIds[i++] = x1 + y1 * cellsPerRow;
                    if (y2 >= 0 && y2 < cellsPerCol)
                        cellIds[i++] = x1 + y2 * cellsPerRow;
                }
                while (i <= 3) cellIds[i++] = -1;

            }
            else if (y1 == y2) {
                int i = 0;
                if (y1 >= 0 && y1 < cellsPerCol) {
                    if (x1 >= 0 && x1 < cellsPerRow)
                        cellIds[i++] = x1 + y1 * cellsPerRow;
                    if (x2 >= 0 && x2 < cellsPerRow)
                        cellIds[i++] = x2 + y1 * cellsPerRow;
                }
                while (i <= 3) cellIds[i++] = -1;

            }
            else {
                int i = 0;
                if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                    cellIds[i++] = x2 + y1 * cellsPerRow;
                if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                    cellIds[i++] = x2 + y2 * cellsPerRow;
                if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                    cellIds[i++] = x1 + y2 * cellsPerRow;

                while (i <= 3) cellIds[i++] = -1;
            }
        }

        else if (obj.bounds instanceof Circle) {

            int x1 = (int) Math.floor((((Circle) obj.bounds).center.x -
                    ((Circle) obj.bounds).radius) / cellSize);
            int x2 = (int) Math.floor((((Circle) obj.bounds).center.x +
                    ((Circle) obj.bounds).radius) / cellSize);
            int y1 = (int) Math.floor((((Circle) obj.bounds).center.y -
                    ((Circle) obj.bounds).radius) / cellSize);
            int y2 = (int) Math.floor((((Circle) obj.bounds).center.y +
                    ((Circle) obj.bounds).radius) / cellSize);

            if (x1 == x2 && y1 == y2) {
                if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
                    cellIds[0] = x1 + y1 * cellsPerRow;
                } else
                    cellIds[0] = -1;
                cellIds[1] = -1;
                cellIds[2] = -1;
                cellIds[3] = -1;

            }
            else if (x1 == x2) {
                int i = 0;
                if (x1 >= 0 && x1 < cellsPerRow) {
                    if (y1 >= 0 && y1 < cellsPerCol)
                        cellIds[i++] = x1 + y1 * cellsPerRow;
                    if (y2 >= 0 && y2 < cellsPerCol)
                        cellIds[i++] = x1 + y2 * cellsPerRow;
                }
                while (i <= 3) cellIds[i++] = -1;

            }
            else if (y1 == y2) {
                int i = 0;
                if (y1 >= 0 && y1 < cellsPerCol) {
                    if (x1 >= 0 && x1 < cellsPerRow)
                        cellIds[i++] = x1 + y1 * cellsPerRow;
                    if (x2 >= 0 && x2 < cellsPerRow)
                        cellIds[i++] = x2 + y1 * cellsPerRow;
                }
                while (i <= 3) cellIds[i++] = -1;

            }
            else {
                int i = 0;
                if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                    cellIds[i++] = x1 + y1 * cellsPerRow;
                if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
                    cellIds[i++] = x2 + y1 * cellsPerRow;
                if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                    cellIds[i++] = x2 + y2 * cellsPerRow;
                if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
                    cellIds[i++] = x1 + y2 * cellsPerRow;

                while (i <= 3) cellIds[i++] = -1;
            }
        }

        return cellIds;
    }
}
