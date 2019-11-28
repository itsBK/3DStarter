package com.bakri.a3dstarter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class OtherActivity extends ListActivity {

    String[] tests = {"Terrain2D", "MarchingCubes", "Terrain3D", "AISimulation",
                      "PointsDistribution2D", "PointsDistribution3D", "GoldenRatioDistribution3D"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, tests));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            Class clazz = Class.forName("com.bakri.a3dstarter.other." + tests[position]);
            startActivity(new Intent(this, clazz));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
