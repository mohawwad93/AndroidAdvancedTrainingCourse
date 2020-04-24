package com.example.todolist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.R;
import com.example.todolist.database.Task;
import com.example.todolist.viewmodel.AddTaskVMFactory;
import com.example.todolist.viewmodel.AddTaskViewModel;

public class AddTaskActivity extends AppCompatActivity {

    private static final int PRIORITY_HIGH = 1;
    private static final int PRIORITY_MED = 2;
    private static final int PRIORITY_LOW = 3;

    private RadioGroup mPriorityRadioGroup;
    private EditText mNameEditText;

    public static final String TASK_ID_EXTRA = "id";
    private static final int DEFAULT_TASK_ID = -1;
    private int mTaskId = DEFAULT_TASK_ID;
    private AddTaskViewModel addTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mNameEditText = findViewById(R.id.name_edittext);
        mPriorityRadioGroup = findViewById(R.id.priority_radiogroup);
        final Button addBtn = findViewById(R.id.add_btn);


        if (getIntent().hasExtra(TASK_ID_EXTRA)) {
            mTaskId = getIntent().getExtras().getInt(TASK_ID_EXTRA);
            if (mTaskId != DEFAULT_TASK_ID) {
                addTaskViewModel = new ViewModelProvider(this, new AddTaskVMFactory(getApplication(), mTaskId)).get(AddTaskViewModel.class);
                addTaskViewModel.getTaskLiveData().observe(this, new Observer<Task>() {
                    @Override
                    public void onChanged(Task task) {
                        addTaskViewModel.getTaskLiveData().removeObserver(this);
                        mNameEditText.setText(task.getName());
                        setPriority(task.getPriority());
                    }
                });

            }

            addBtn.setText("Update");
        }else{
            addTaskViewModel = new ViewModelProvider(this, new AddTaskVMFactory(getApplication(), mTaskId)).get(AddTaskViewModel.class);
        }


    }

    public void onAddBtnClicked(View view) {

        final Task task = new Task(mNameEditText.getText().toString(), System.currentTimeMillis(), getPriority());
        if (mTaskId == DEFAULT_TASK_ID) {
           addTaskViewModel.insert(task);
        } else {
            task.setId(mTaskId);
            addTaskViewModel.update(task);
        }

        finish();
    }


    private int getPriority() {

        int selectedPriorityRadioBtn = mPriorityRadioGroup.getCheckedRadioButtonId();
        switch (selectedPriorityRadioBtn) {
            case R.id.high_radiobtn:
                return PRIORITY_HIGH;
            case R.id.med_radiobtn:
                return PRIORITY_MED;
            case R.id.low_radiobtn:
                return PRIORITY_LOW;
        }
        return 0;
    }

    private void setPriority(int priority) {

        switch (priority) {
            case PRIORITY_HIGH:
                mPriorityRadioGroup.check(R.id.high_radiobtn);
                break;
            case PRIORITY_MED:
                mPriorityRadioGroup.check(R.id.med_radiobtn);
                break;
            case PRIORITY_LOW:
                mPriorityRadioGroup.check(R.id.low_radiobtn);
                break;
        }
    }

}
