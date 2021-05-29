package com.example.breaths;

public class Pollutant {
    int columnPollutantId,columnPollutantLow,columnPollutantMedium,columnPollutantHigh,columnPollutantDanger;
    String columnPollutant;

    public Pollutant(int columnPollutantId ,String columnPollutant,int columnPollutantLow,int columnPollutantMedium,int columnPollutantHigh,int columnPollutantDanger) {
        this.columnPollutantId = columnPollutantId;
        this.columnPollutant = columnPollutant;
        this.columnPollutantLow = columnPollutantLow;
        this.columnPollutantMedium = columnPollutantMedium;
        this.columnPollutantHigh = columnPollutantHigh;
        this.columnPollutantDanger = columnPollutantDanger;
    }
}
