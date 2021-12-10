package org.openjfx.models;

import org.openjfx.UiInterface;

import java.util.List;

public class Robot implements Runnable {
    private String name;
    private int size;
    private String status;
    private int time;
    private final List<PositionModel> list;
    private int currentPosition = -1;
    private UiInterface uiInterface;
    private int freeId = 0;
    //status
    private static final String WORKING = "dziala";
    private static final String WAITING = "czeka";
    private static final String CHARGING = "laduje";


    public Robot(String name, int size, String status, int time, List<PositionModel> list, UiInterface uiInterface) {
        this.name = name;
        this.size = size;
        this.status = status;
        this.time = time;
        this.list = list;
        this.uiInterface = uiInterface;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void produce() throws InterruptedException {
        synchronized (list) {
            while (!hasFreeSlots()) {
                status = WAITING;
                uiInterface.update(-1, "", status, name, size);
                list.wait();
            }
            list.get(freeId).setRobotName(name);
            status = CHARGING;
            if (!list.get(freeId).isEdge()) setEmpty(freeId, size, false);
            else list.get(freeId).setEmpty(false);
            //list.notifyAll();
            uiInterface.update(freeId, freeId + 1 + ": " + name, status, name, size);
        }
        Thread.sleep(time * 100);
        synchronized (list) {
            list.get(freeId).setRobotName("");
            if (!list.get(freeId).isEdge()) setEmpty(freeId, size, true);
            else list.get(freeId).setEmpty(true);
            status = WORKING;
            list.notifyAll();
            uiInterface.update(freeId, freeId + 1 + ": --", status, name, size);
        }
        Thread.sleep(time * 100);
    }


    private void setEmpty(int id, int size, boolean status) {
        for (int i = 0; i < size; i++) {
            list.get(id - i).setEmpty(status);
        }
    }

    private boolean hasFreeSlots() {
        int i = 0;
        for (PositionModel model : list) {
            if (model.isEmpty()) {
                i++;
                if (i == size || model.isEdge()) {
                    freeId = findFreeId(model);
                    return true;
                }
            } else {
                i = 0;
            }

        }
        return false;
    }

    private int findFreeId(PositionModel model) {
        return list.indexOf(model);
    }
}
