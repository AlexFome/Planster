package com.fome.planster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fome.planster.models.List;
import com.fome.planster.adapters.ListItemsAdapter;
import com.fome.planster.models.Task;
import com.google.gson.Gson;

public class ListActivity extends AppCompatActivity {

    private List list;
    private Task task;
    private ListView listView;
    private ListItemsAdapter adapter;
    private TextView listCustomizationFinishButton;

    boolean editable;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FontManager.setFont(this, findViewById(R.id.activity_day), FontManager.BOLDFONT);
        FontManager.setFont(this, findViewById(R.id.list_sign), FontManager.BOLDFONT);

        listView = (ListView) findViewById(R.id.listview);
        listCustomizationFinishButton = (TextView) findViewById(R.id.finish_list_customization_button);
        listCustomizationFinishButton.setTypeface(FontManager.getTypeface(this, FontManager.ICONFONT));
        listCustomizationFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveList();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task")) {
            // List activity is called from day activity, list items can't be edited
            task = gson.fromJson(intent.getExtras().getString("task"), Task.class);
            list = task.getList();
            editable = false;
        } else {
            // List activity is called from task creation activity, list items can be edited
            list = gson.fromJson(getIntent().getExtras().getString("list"), List.class);
            editable = true;
            if (list.isEmpty()) {
                list.initEmptySpaces();
            }
        }
        adapter = new ListItemsAdapter(this, list, listView, editable);
        listView.setAdapter(adapter);

    }

    void saveList () {
        Intent intent = new Intent();
        if (editable) {
            intent.putExtra("list", gson.toJson(list));
        } else {
            task.setList(list);
            intent.putExtra("task", gson.toJson(task));
        }
        setResult(RESULT_OK, intent);
        finish();
    }
 }
