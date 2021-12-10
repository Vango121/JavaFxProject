package org.openjfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.openjfx.models.PositionModel;
import org.openjfx.models.Robot;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulatorScene implements Initializable, UiInterface {

    @FXML
    GridPane gridPane;
    List<PositionModel> positionModelList;
    List<Label> positionsLabelsList;
    List<Robot> robotList;
    List<Label> statusLabel;
    private int positionsCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionModelList = new ArrayList<>();
        positionsLabelsList = new ArrayList<>();
        robotList = new ArrayList<>();
        statusLabel = new ArrayList<>();
    }

    private int getGridPositionsCount(int positionsCount) {
        int count = 1;
        for (int i = 1; i < positionsCount; i++) {
            count += 3;
        }
        return count;
    }

    public void setItems(int robotsCount, int positionsCount) {
        Label stanowiska = new Label("Stanowiska");
        Label roboty = new Label("Roboty");
        this.positionsCount = positionsCount;
        gridPane.getColumnConstraints()
                .add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
        gridPane.add(stanowiska, 0, 0);
        gridPane.add(roboty, 1, 0);
        //System.out.println(getGridPositionsCount(positionsCount));
        setUpPositions(positionsCount, getGridPositionsCount(positionsCount));
        setUpRobots(robotsCount, positionsCount);
    }

    public void setUpRobots(int robotsCount, int positionsCount) {

        int name = 65;
        for (int i = 0; i < robotsCount; i++) {
            int size = (int) ((Math.random() * (positionsCount)) + 1);
            int margin = (int) ((Math.random() * (10)));
            Robot robot = new Robot(String.valueOf((char) name), size, "dziala", size * 10 + margin, positionModelList, this);
            robotList.add(robot);
            name++;
        }
        setUpName(robotList);
        setUpSize(robotList);
        setUpStatus(robotList);
        setUpTime(robotList);
        startThreads();
    }

    public void startThreads() {
        for (Robot robot : robotList) {
            Thread tRobot = new Thread(robot, robot.getName());
            tRobot.setDaemon(true);
            tRobot.start();
        }
    }

    public void setUpName(List<Robot> list) {
        gridPane.getColumnConstraints()
                .add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
        Label nazwa = new Label("Nazwa");
        gridPane.add(nazwa, 1, 1);
        for (int i = 0; i < list.size(); i++) {
            Label label = new Label(list.get(i).getName());
            gridPane.add(label, 1, i + 2);
        }
    }

    public void setUpSize(List<Robot> list) {
        gridPane.getColumnConstraints()
                .add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
        Label size = new Label("Rozmiar");
        gridPane.add(size, 2, 1);
        for (int i = 0; i < list.size(); i++) {
            Label label = new Label(String.valueOf(list.get(i).getSize()));
            gridPane.add(label, 2, i + 2);
        }
    }

    public void setUpStatus(List<Robot> list) {
        gridPane.getColumnConstraints()
                .add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
        Label status = new Label("Status");
        gridPane.add(status, 3, 1);
        for (int i = 0; i < list.size(); i++) {
            Label label = new Label(list.get(i).getStatus());
            statusLabel.add(label);
            gridPane.add(label, 3, i + 2);
        }
    }

    public void setUpTime(List<Robot> list) {
        gridPane.getColumnConstraints()
                .add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
        Label czas = new Label("Czas");
        gridPane.add(czas, 4, 1);
        for (int i = 0; i < list.size(); i++) {
            Label label = new Label(String.valueOf(list.get(i).getTime()));
            gridPane.add(label, 4, i + 2);
        }
    }

    public void setUpPositions(int positionsCount, int gridPositionsCount) {
        int index = 0;

        for (int i = 1; i <= gridPositionsCount; i++) {
            gridPane.getRowConstraints()
                    .add(new RowConstraints(-1, -1, -1, Priority.ALWAYS, VPos.CENTER, false));
            if (i >= positionsCount && i < 2 * positionsCount) {
                index++;
                if (i == positionsCount || i == 2 * positionsCount - 1) {
                    PositionModel positionModel = new PositionModel(index, true, true);
                    positionModelList.add(positionModel);
                    Label label = new Label(positionModel.toString());
                    positionsLabelsList.add(label);
                    gridPane.add(label, 0, i);
                } else {
                    PositionModel positionModel = new PositionModel(index, true, false);
                    positionModelList.add(positionModel);
                    Label label = new Label(positionModel.toString());
                    positionsLabelsList.add(label);
                    gridPane.add(label, 0, i);
                }
            } else {
                Label label = new Label("--");
                positionsLabelsList.add(label);
                gridPane.add(label, 0, i);
            }
        }
    }


    public int findRobotId(String robotName) {
        for (int i = 0; i < robotList.size(); i++) {
            if (robotList.get(i).getName().equals(robotName)) return i;
        }
        return -1;
    }

    private void startingEdge(String position, String robotName, int size, int firstPosition) { // first position on edge
        Pattern pattern = Pattern.compile("\\d: --");
        boolean patternMatcher = pattern.matcher(position).matches();
        if (patternMatcher) {
            for (int i = 0; i < firstPosition; i++) {
                positionsLabelsList.get(i).setText("--");
            }
        } else {
            int temp = 1; // var to check amount of slots taken
            int tempId = firstPosition - 1;
            while (temp != size) {
                positionsLabelsList.get(tempId).setText("-- " + robotName);
                tempId--;
                temp++;
            }
        }
    }

    private void endEdge(String position, String robotName, int size, int lastPosition) {
        Pattern pattern = Pattern.compile("\\d: --");
        boolean patternMatcher = pattern.matcher(position).matches();
        if (patternMatcher) {
            for (int i = lastPosition + 1; i < positionsLabelsList.size(); i++) {
                positionsLabelsList.get(i).setText("--");
            }
        } else {
            int temp = 1; // var to check amount of slots taken
            int tempId = lastPosition + 1;
            while (temp != size) {
                positionsLabelsList.get(tempId).setText("-- " + robotName);
                tempId++;
                temp++;
            }
        }

    }

    private void drawInMiddle(int id, String position, String robotName, int size, int startId) {
        if (!positionModelList.get(startId).isEdge()) {
            Pattern pattern = Pattern.compile("\\d: --");
            Pattern patternId = Pattern.compile("\\d: ");
            boolean patternMatcher = pattern.matcher(position).matches();
            for (int i = id - size + 1; i < id; i++) {
                Matcher matcher = patternId.matcher(positionsLabelsList.get(i).getText());
                if (!patternMatcher) {
                    if (matcher.find()) {
                        positionsLabelsList.get(i).setText(matcher.group(0) + robotName);
                    }
                } else {
                    if (matcher.find()) {
                        positionsLabelsList.get(i).setText(matcher.group(0) + "--");
                    }
                }
            }
        }
    }

    @Override
    public void update(int id, String position, String status, String robotName, int size) {
        if (id > -1) {
            Platform.runLater(() -> {
                int firstPosition = id + positionsCount - 1;

                if (positionModelList.get(id).isEdge() && id == 0) {
                    startingEdge(position, robotName, size, firstPosition);
                } else if (positionModelList.get(id).isEdge() && id == positionsCount - 1)
                    endEdge(position, robotName, size, firstPosition);
                positionsLabelsList.get(firstPosition).setText(position);
                drawInMiddle(firstPosition, position, robotName, size, id);
                statusLabel.get(findRobotId(robotName)).setText(status);
            });

        } else {
            Platform.runLater(() -> statusLabel.get(findRobotId(robotName)).setText(status));
        }
    }

}
