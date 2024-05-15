package com.cursedastronaut.plainhome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<ResolveInfo> apps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view tp res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        //Get ListView by its id in activity_main
        listView = findViewById(R.id.app_list);

        //Get PackageManager to get the apps
        final PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // Query for all apps that can be launched from the home screen
        apps = pm.queryIntentActivities(mainIntent, 0);

        // Sort the apps list based on the app names
        Collections.sort(apps, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                String appName1 = o1.loadLabel(pm).toString();
                String appName2 = o2.loadLabel(pm).toString();
                return appName1.compareTo(appName2);
            }
        });

        // Extract the sorted app names into a separate list if needed
        List<String> appNames = new ArrayList<>();
        for (ResolveInfo resolveInfo : apps) {
            String appName = resolveInfo.loadLabel(pm).toString();
            appNames.add(appName);
        }/*
        apps = pm.queryIntentActivities(mainIntent, 0);

        List<String> appNames = new ArrayList<>();
        for (ResolveInfo resolveInfo : apps) {
            String appName = resolveInfo.loadLabel(pm).toString();
            appNames.add(appName);
        }
        Collections.sort(appNames);*/
        //appNames.add("Définir comme launcher par défaut");

        LinearLayout containerApps = findViewById(R.id.container_apps);
        containerApps.setVisibility(View.GONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo resolveInfo = apps.get(position);
                Intent launchIntent = pm.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
                //promptSetDefaultLauncher();
            }
        });
    }

    public void SeeList(View view) {
        LinearLayout containerApps  = findViewById(R.id.container_apps);
        LinearLayout containerIntro = findViewById(R.id.container_intro);
        containerApps.setVisibility(View.VISIBLE);
        containerIntro.setVisibility(View.GONE);
    }

    public void promptSetDefaultLauncher(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        startActivity(intent);
        try {



        } catch (ActivityNotFoundException e) {
            Log.e("LauncherError", "Home activity not found", e);
            Toast.makeText(this, "Home activity not found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("LauncherError", "Unexpected error", e);
            Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT).show();
        }
    }
}
