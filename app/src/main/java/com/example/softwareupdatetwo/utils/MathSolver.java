package com.example.softwareupdatetwo.utils;

import android.annotation.SuppressLint;
import android.content.Context;

public class MathSolver {

    Context context;

    public MathSolver(Context context) {
        this.context = context;
    }

    public MathSolver() {
    }

    @SuppressLint("DefaultLocale")
    public String getCalculatedDataSizeWithPrefix(float size) {
        String sizePrefix = "Bytes";
        float finalSize = size;
        if (size >= 1024) {
            float sizeKb = size / 1024;
            sizePrefix = "KB";
            finalSize = sizeKb;
            if (sizeKb >= 1024) {
                float sizeMB = sizeKb / 1024;
                sizePrefix = "MB";
                finalSize = sizeMB;
                if (sizeMB >= 1024) {
                    float sizeGb = sizeMB / 1024;
                    sizePrefix = "GB";
                    finalSize = sizeGb;
                } } }
        return String.format("%.2f", finalSize) + sizePrefix;
    }
    public String getCalculatedDataSizeFloat(float size) {
         float finalSize = size;
        if (size >= 1024) {
            float sizeKb = size / 1024;
             finalSize = sizeKb;
            if (sizeKb >= 1024) {
                float sizeMB = sizeKb / 1024;
                 finalSize = sizeMB;
                if (sizeMB >= 1024) {
                    float sizeGb = sizeMB / 1024;
                     finalSize = sizeGb;
                } } }
        return String.format("%.2f", finalSize);
    }

    public float getFahrenheitToCelsiusFloat(float fahren) {
        return (fahren - 32) * 5 / 9;

    }
    public static int getCelsiusToFahrenheit(int celsius)
    {
        return (celsius * 9)/5 + 32;
    }
    public float getPercentageFloat(float totalData, float usedData) {

        return (usedData * 100 / totalData);
    }
}

