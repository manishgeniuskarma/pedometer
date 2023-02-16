package com.queens.sqliteloginandsignup.utility;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class MyFillFormatter implements IFillFormatter
{

    private float fillPos;

    public MyFillFormatter(float fillPos) {
        this.fillPos = fillPos;
    }

    @Override
    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
        // your logic could be here
        return fillPos;
    }
}
