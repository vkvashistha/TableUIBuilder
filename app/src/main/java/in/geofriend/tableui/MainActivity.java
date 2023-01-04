package in.geofriend.tableui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import in.geofriend.tableuibuilder.TableUIBuilder;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        example1();
//        example2();
//        example3();
    }

    private void example1() {
        ((ViewGroup) findViewById(R.id.tableContainer)).addView(
                new TableUIBuilder(this, "students_records.csv")
                        .setDefaultCellBackgroundColor(Color.WHITE)
                        .setRowBackgroundColor(Color.LTGRAY,0) // setting background color of 0th row
                        .setRowTextStyle(Typeface.BOLD,0) // setting text style of 0th row
                        .setRowTextColor(Color.DKGRAY, 0) // setting text color of 0th Row
                        .build());
    }

    private void example2() {
        ((ViewGroup) findViewById(R.id.tableContainer)).addView(
                new TableUIBuilder(this, "students_records_dynamic.csv")
                        .setDefaultCellBackgroundColor(Color.WHITE)
                        .setRowBackgroundColor(Color.GRAY,0)
                        .setRowTextStyle(Typeface.BOLD,0)
                        .setRowTextColor(Color.DKGRAY, 0)
                        .bind(getStudentsDataAdapter(getStudentData())) // data binding
                        .build());
    }

    private void example3() {
        ((ViewGroup) findViewById(R.id.tableContainer)).addView(new TableUIBuilder(this, "students_records_dynamic1.csv")
                .bind(getStudentsDataAdapter1(getStudentData()))
                .setDefaultCellBackgroundColor(Color.WHITE)
                .setRowBackgroundColor(Color.GRAY,0)
                .setRowTextStyle(0, Typeface.BOLD)
                .setRowTextColor(Color.DKGRAY, 0)
                .setRowBackgroundColor(Color.LTGRAY,1)
                .build());
    }

    private TableUIBuilder.TableDataAdapter getStudentsDataAdapter(List<Student> students) {
        return new TableUIBuilder.TableDataAdapter() {
            @Override
            public int getRowCount(int templateRowIndex) {
                if(templateRowIndex == 1) {
                    return students.size();
                }
                return super.getRowCount(templateRowIndex);
            }

            @Override
            public String getValue(String columnName, int templateRowIndex, int index) {
                if(templateRowIndex == 1) {
                    switch (columnName) {
                        case "student_name": return students.get(index).name;
                        case "course": return students.get(index).course;
                        case "score": return students.get(index).score;
                    }
                }
                return super.getValue(columnName, templateRowIndex, index);
            }
        };
    }

    private TableUIBuilder.TableDataAdapter getStudentsDataAdapter1(List<Student> students) {
        return new TableUIBuilder.TableDataAdapter() {
            @Override
            public int getRowCount(int templateRowIndex) {
                if(templateRowIndex == 2) {
                    return students.size();
                }
                return super.getRowCount(templateRowIndex);
            }

            @Override
            public String getValue(String columnName, int templateRowIndex, int index) {
                if(templateRowIndex == 0) {
                    switch (columnName) {
                        case "school_name":return "Stanford University";
                        case "city":return "Stanford";
                        case "school_rank": return "1";
                    }
                } else if(templateRowIndex == 2) {
                    switch (columnName) {
                        case "student_name": return students.get(index).name;
                        case "course": return students.get(index).course;
                        case "score": return students.get(index).score;
                    }
                }
                return super.getValue(columnName, templateRowIndex, index);
            }
        };
    }

    private TableUIBuilder.TableDataAdapter getAdapter() {
        return new TableUIBuilder.TableDataAdapter() {
            @Override
            public String getValue(String columnName, int templateRowIndex, int index) {
                if(templateRowIndex == 0) {
                    return columnName + ": " + index;
                } else if(templateRowIndex == 2) {
                    return columnName + ": " + index;
                }
                return super.getValue(columnName, templateRowIndex, index);
            }

            @Override
            public int getRowCount(int templateRowIndex) {
                if(templateRowIndex == 2) {
                    return 5;
                }
                return super.getRowCount(templateRowIndex);
            }
        };


    }

    private List<Student> getStudentData() {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("Raghvan", "Ancient Mythology", "100"));
        students.add(new Student("Krishna", "Politics", "98"));
        students.add(new Student("Chanakya", "Economics", "98"));
        return students;
    }

    static class Student {
        String name, course, score;
        public Student(String name, String course, String score) {
            this.name = name;
            this.course = course;
            this.score = score;
        }
    }
}