package org.openjfx.models;

public class PositionModel {
    private int id;
    private boolean isEmpty;
    private String robotName = "";
    private boolean edge;

    public PositionModel(int id, boolean isEmpty) {
        this.id = id;
        this.isEmpty = isEmpty;
    }

    public PositionModel(int id, boolean isEmpty, boolean edge) {
        this.id = id;
        this.isEmpty = isEmpty;
        this.edge = edge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public boolean isEdge() {
        return edge;
    }

    public void setEdge(boolean edge) {
        this.edge = edge;
    }

    @Override
    public String toString() {
        return id + ": " + robotName;
    }
}
