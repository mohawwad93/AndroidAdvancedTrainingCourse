 package com.example.todolist.activities;

 import android.content.Intent;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.lifecycle.Observer;
 import androidx.lifecycle.ViewModelProvider;
 import androidx.recyclerview.widget.DividerItemDecoration;
 import androidx.recyclerview.widget.ItemTouchHelper;
 import androidx.recyclerview.widget.RecyclerView;

 import com.example.todolist.AppExecutor;
 import com.example.todolist.R;
 import com.example.todolist.adapters.TaskAdapter;
 import com.example.todolist.database.AppDatabase;
 import com.example.todolist.database.Task;

 import com.example.todolist.viewmodel.MainVMFactory;
 import com.example.todolist.viewmodel.MainViewModel;
 import com.google.android.material.floatingactionbutton.FloatingActionButton;

 import java.util.List;

 import static com.example.todolist.activities.AddTaskActivity.TASK_ID_EXTRA;

 public class MainActivity extends AppCompatActivity {

     private static final String TAG = MainActivity.class.getSimpleName();
     private TaskAdapter mTaskAdapter;
     private MainViewModel mainViewModel;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fba = findViewById(R.id.floatingActionButton);
        fba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        mTaskAdapter = new TaskAdapter(this, new TaskAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(Task task) {
                Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
                i.putExtra(TASK_ID_EXTRA, task.getId());
                startActivity(i);
            }
        });

        recyclerView.setAdapter(mTaskAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Task task = mTaskAdapter.getTask(position);
                mainViewModel.deleteTask(task);
            }
        }).attachToRecyclerView(recyclerView);


          mainViewModel = new ViewModelProvider(this, new MainVMFactory(getApplication()))
                                            .get(MainViewModel.class);

         mainViewModel.getListTaskLiveData().observe(this, new Observer<List<Task>>() {
             @Override
             public void onChanged(List<Task> tasks) {
                 Log.i(TAG, "onChanged");
                 mTaskAdapter.setTasks(tasks);
             }
         });

     }
 }
