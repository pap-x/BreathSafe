package com.example.breaths;

public class Text {
    String columnConditionIdList;
    int columnPollutantsId, columnIndex,columnTextId;
    String columnText;

    public Text(String columnConditionIdList, int columnPollutantsId, int columnIndex, String columnText, int columnTextId) {
        this.columnConditionIdList = columnConditionIdList;
        this.columnPollutantsId = columnPollutantsId;
        this.columnIndex = columnIndex;
        this.columnText = columnText;
        this.columnTextId = columnTextId;

    }
}
