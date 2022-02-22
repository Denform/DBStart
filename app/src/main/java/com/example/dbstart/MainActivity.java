package com.example.dbstart;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    Button addedData, readData, updateData, deleteData;
    ImageButton added, clearData;
    EditText inputName, inputDepartment, inputSalary, inputId, search;
    RadioButton sortId, sortDepart, sortSalary;
    TextView result;
    Toast toast;
    DBHelper dbHelper;
    String[] items, items2;
    String orderBy = "_id";
    Cursor cursor;
    ListView listView;
    EditText editText;
    ArrayAdapter<String> adapter;
    final Context context = this;
    final ArrayList<String> contDataBase = new ArrayList<>();
    ArrayList<Integer> idDB = new ArrayList<>();
    ArrayList<String> forName = new ArrayList<>();
    int check = 0;
    int[] checkSort = new int[] {1,0,0};

    public MainActivity() {
        adapter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addedData = (Button) findViewById(R.id.addedData);
        addedData.setOnClickListener(this);

        readData = (Button) findViewById(R.id.readData);
        readData.setOnClickListener(this);

        clearData = (ImageButton) findViewById(R.id.clearData);
        clearData.setOnClickListener(this);

        updateData = (Button) findViewById(R.id.updateData);
        updateData.setOnClickListener(this);

        deleteData = (Button) findViewById(R.id.deleteData);
        deleteData.setOnClickListener(this);

        inputName = (EditText) findViewById(R.id.inputName);
        search = (EditText) findViewById(R.id.searchName);
        inputDepartment = (EditText) findViewById(R.id.inputDepartment);
        inputSalary = (EditText) findViewById(R.id.inputSalary);
        inputId = (EditText) findViewById(R.id.etId);

        sortId = (RadioButton) findViewById(R.id.sortId);
        sortId.setOnClickListener(this);

        sortDepart = (RadioButton) findViewById(R.id.sortDepart);
        sortDepart.setOnClickListener(this);

        sortSalary = (RadioButton) findViewById(R.id.sortSalary);
        sortSalary.setOnClickListener(this);


        dbHelper = new DBHelper(this);

        listView = (ListView) findViewById(R.id.listView);
        editText = (EditText) findViewById(R.id.inputName);

        registerForContextMenu(listView);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        cursor = database.query(DBHelper.TABLE_Shop, null, null, null, null, null, orderBy);
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_Id);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NameWorker);
        int departIndex = cursor.getColumnIndex(DBHelper.KEY_Department);
        int salaryIndex = cursor.getColumnIndex(DBHelper.KEY_Salary);
        while (cursor.moveToNext()) {
            // Используем индекс для получения строки или числа
            int currentID = cursor.getInt(idIndex);
            String currentName = cursor.getString(nameIndex);
            String currentDep = cursor.getString(departIndex);
            int currentSal = cursor.getInt(salaryIndex);
            // Выводим значения каждого столбца
            forName.add("\n" + "Name = " + cursor.getString(nameIndex) +
                    "\n" + "Department = " + cursor.getString(departIndex) +
                    "\n" + "Salary = " + cursor.getInt(salaryIndex) + "\n");
            contDataBase.add(cursor.getString(nameIndex));
            idDB.add(cursor.getInt(idIndex));
        }

//        for (int i = 0; i < contDataBase.size(); i++)
//        {
//            search.append(forName.get(i).toString());
//        }
        if (contDataBase.size() == 0)
        {
                contDataBase.add("Contain Your DataBase \n 0 rows");
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contDataBase);
        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);

        // Прослушиваем нажатия клавиш
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        contDataBase.add(editText.getText().toString());
                        adapter.notifyDataSetChanged();
                        editText.setText("");
                        return true;
                    }
                return false;
            }
        });
        //Инициализируем элементы:
        added = (ImageButton) findViewById(R.id.Added);
        //Добавляем слушателя нажатий по кнопке Button:
        added.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText inputName3 = (EditText) promptsView.findViewById(R.id.inputName3);
                final EditText inputSalary3 = (EditText) promptsView.findViewById(R.id.inputSalary3);
                final EditText inputDepartment3 = (EditText) promptsView.findViewById(R.id.inputDepartment3);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                                        search.setText("");
                                        contDataBase.clear();
                                        String name = inputName3.getText().toString();
                                        String depart = inputDepartment3.getText().toString();
                                        String salary = inputSalary3.getText().toString();
//                                        String id = inputId.getText().toString();

                                        ContentValues contentValues = new ContentValues();
                                        //Вводим текст и отображаем в строке ввода на основном экране:
                                        contentValues.put(DBHelper.KEY_NameWorker, name);
                                        contentValues.put(DBHelper.KEY_Department, depart);
                                        contentValues.put(DBHelper.KEY_Salary, salary);

                                        database.insert(DBHelper.TABLE_Shop, null, contentValues);
                                        check = 0;
                                        inputName.setText("");
                                        inputDepartment.setText("");
                                        inputSalary.setText("");
                                        adapter.notifyDataSetChanged();

                                        cursor = database.query(DBHelper.TABLE_Shop, null, null, null, null, null, orderBy);
                                        if (cursor.moveToFirst()) {
                                            int idIndex = cursor.getColumnIndex(DBHelper.KEY_Id);
                                            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NameWorker);
                                            int departIndex = cursor.getColumnIndex(DBHelper.KEY_Department);
                                            int salaryIndex = cursor.getColumnIndex(DBHelper.KEY_Salary);
                                            if (check == 0)
                                            {
                                                idDB.clear();
                                                forName.clear();
                                                do {
                                                    forName.add("Name = " + cursor.getString(nameIndex) +
                                                            "\n" + "Department = " + cursor.getString(departIndex) +
                                                            "\n" + "Salary = " + cursor.getInt(salaryIndex) + "\n");
                                                    contDataBase.add(cursor.getString(nameIndex));
                                                    idDB.add(cursor.getInt(idIndex));
                                                } while (cursor.moveToNext());
//                                                search.setText("");
//                                                for (int i = 0; i < contDataBase.size(); i++)
//                                                {
//                                                    search.append(forName.get(i).toString());
//                                                }
                                                items2 = new String[contDataBase.size()];
                                                for(int i = 0; i < contDataBase.size(); i++)
                                                {
                                                    items2[i] = contDataBase.get(i);
                                                }
                                                initList();
                                                check++;
                                                adapter.notifyDataSetChanged();
                                            }
                                        } else
                                        if (check == 0)
                                        {
                                            contDataBase.add("Contain Your DataBase \n 0 rows");
                                        }
                                        check++;
                                        cursor.close();
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //и отображаем его:
                alertDialog.show();

            }
        });
// ---------------------------------------------------------------------------------------
        items2 = new String[contDataBase.size()];
        for(int i = 0; i < contDataBase.size(); i++)
        {
            items2[i] = contDataBase.get(i);
        }

        initList();

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    // reset listview
                    initList();
                } else {
                    // perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        readData.performClick();
    }

    public void searchItem(String textToSearch){
        for(String item:items){
            if(!item.contains(textToSearch)){
                contDataBase.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void initList(){
        items = new String[items2.length];
        for(int i = 0; i < items2.length; i++)
        {
            items[i] = items2[i];
        }
//        listItems = new ArrayList<>(Arrays.asList(items));
        contDataBase.clear();
        for(int i = 0; i < items2.length; i++)
        {
            contDataBase.add(i,items[i]);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contDataBase);
        listView.setAdapter(adapter);
    }
//    ---------------------------------------------------------------------------------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contDataBase.clear();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
//                editItem(); // метод, выполняющий действие при редактировании пункта меню
                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);


                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int idd) {
                                        //Настраиваем отображение поля для ввода текста в открытом диалоге:
                                        final EditText inputName3 = (EditText) promptsView.findViewById(R.id.inputName3);
                                        final EditText inputSalary3 = (EditText) promptsView.findViewById(R.id.inputSalary3);
                                        final EditText inputDepartment3 = (EditText) promptsView.findViewById(R.id.inputDepartment3);

                                        String name = inputName3.getText().toString();
                                        String depart = inputDepartment3.getText().toString();
                                        String salary = inputSalary3.getText().toString();
                                        if ((DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_Shop)) == 0) {
                                            return;
                                        }

                                        contentValues.put(DBHelper.KEY_Salary, salary);
                                        contentValues.put(DBHelper.KEY_NameWorker, name);
                                        contentValues.put(DBHelper.KEY_Department, depart);
                                        database.update(DBHelper.TABLE_Shop, contentValues, DBHelper.KEY_Id + "= ?", new String[]{String.valueOf(idDB.get(info.position))});
//                                        toast = Toast.makeText(getApplicationContext(),
//                                                "Apdate rows with id = " + (idDB.get(info.position)) + name +"name", Toast.LENGTH_SHORT);
//                                        toast.show();
                                        check = 0;
                                        readData.performClick();

                                        inputName3.setText("");
                                        inputDepartment3.setText("");
                                        inputSalary3.setText("");

                                        items2 = new String[contDataBase.size()];
                                        for(int i = 0; i < contDataBase.size(); i++)
                                        {
                                            items2[i] = contDataBase.get(i);
                                        }
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        listView.requestLayout();
                                        readData.performClick();
                                    }
                                });
                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //и отображаем его:
                alertDialog.show();
                return true;
            case R.id.delete:

                if ((DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_Shop)) == 0) {
                    return true;
                }

                database.delete(DBHelper.TABLE_Shop, DBHelper.KEY_Id + "=" + idDB.get(info.position), null);
//                adapter.notifyDataSetChanged();
                check = 0;

//                toast = Toast.makeText(getApplicationContext(),
//                        "Deleted rows with id = " + (idDB.get(info.position)), Toast.LENGTH_SHORT);
//                toast.show();
                idDB.remove(info.position);
                if (idDB.size() == 0)
                {
                    contDataBase.clear();
                    adapter.notifyDataSetChanged();
                }
                readData.performClick();
                items2 = new String[contDataBase.size()];
                for(int i = 0; i < contDataBase.size(); i++)
                {
                    items2[i] = contDataBase.get(i);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v)
    {

        String name = inputName.getText().toString();
        String depart = inputDepartment.getText().toString();
        String salary = inputSalary.getText().toString();
        String id = inputId.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

//                Toast.makeText(getApplicationContext(), forName.get(position),
//                        Toast.LENGTH_SHORT).show();

                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.fullinfo, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final TextView fullInfoPers = (TextView) promptsView.findViewById(R.id.fullinfo);
                fullInfoPers.setText(forName.get(position));
                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //и отображаем его:
                alertDialog.show();
            }
        });

        switch (v.getId())
        {

            case R.id.addedData:
                contDataBase.clear();

                contentValues.put(DBHelper.KEY_NameWorker, name);
                contentValues.put(DBHelper.KEY_Department, depart);
                contentValues.put(DBHelper.KEY_Salary, salary);

                database.insert(DBHelper.TABLE_Shop, null, contentValues);
                check = 0;
                inputName.setText("");
                inputDepartment.setText("");
                inputSalary.setText("");
                adapter.notifyDataSetChanged();

                cursor = database.query(DBHelper.TABLE_Shop, null, null, null, null, null, orderBy);
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_Id);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NameWorker);
                    int departIndex = cursor.getColumnIndex(DBHelper.KEY_Department);
                    int salaryIndex = cursor.getColumnIndex(DBHelper.KEY_Salary);
                    if (check == 0)
                    {
                        do {
                            contDataBase.add("Name = " + cursor.getString(nameIndex) +
                                    "\n" + "Department = " + cursor.getString(departIndex) +
                                    "\n" + "Salary = " + cursor.getInt(salaryIndex) + "\n");

                        } while (cursor.moveToNext());
//                        search.setText("");
//                        for (int i = 0; i < contDataBase.size(); i++)
//                        {
//                            search.append(idDB.get(i).toString());
//                        }
                        check++;
                        adapter.notifyDataSetChanged();
                    }
                } else
                if (check == 0)
                {
                    contDataBase.add("Contain Your DataBase \n 0 rows");
                }
                check++;
                cursor.close();
                break;

            case R.id.readData:
                search.setText("");
                contDataBase.clear();
                cursor = database.query(DBHelper.TABLE_Shop, null, null, null, null, null, orderBy);
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_Id);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NameWorker);
                    int departIndex = cursor.getColumnIndex(DBHelper.KEY_Department);
                    int salaryIndex = cursor.getColumnIndex(DBHelper.KEY_Salary);
                    if (check == 0)
                    {
                        idDB.clear();
                        forName.clear();
                        do {
                            forName.add("Name = " + cursor.getString(nameIndex) +
                                    "\n" + "Department = " + cursor.getString(departIndex) +
                                    "\n" + "Salary = " + cursor.getInt(salaryIndex) + "\n");
                            contDataBase.add(cursor.getString(nameIndex));
                            idDB.add(cursor.getInt(idIndex));
                        } while (cursor.moveToNext());
//                        search.setText("");
//                        for (int i = 0; i < contDataBase.size(); i++)
//                        {
//                            search.append(forName.get(i).toString());
//                        }
                        check++;
                        adapter.notifyDataSetChanged();
                    }
                } else
                    if (check == 0)
                    {
                        contDataBase.add("Contain Your DataBase \n 0 rows");
                    }
                    check++;
                cursor.close();
                break;

            case R.id.clearData:
                database.delete(DBHelper.TABLE_Shop, null, null);
                contDataBase.clear();
                contDataBase.add("Contain Your DataBase \n 0 rows");
                adapter.notifyDataSetChanged();
                check = 0;
                break;

            case R.id.deleteData:

                if ((id.equalsIgnoreCase("")) || (DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_Shop)) == 0)
                {
                    break;
                }

                database.delete(DBHelper.TABLE_Shop, DBHelper.KEY_Id + "=" + id, null);
//                adapter.notifyDataSetChanged();
                check = 0;
                readData.performClick();

                toast = Toast.makeText(getApplicationContext(),
                "Deleted rows with id = " + inputId.getText(), Toast.LENGTH_SHORT);
                toast.show();

                inputId.setText("");
                break;

            case R.id.updateData:
                if ((id.equalsIgnoreCase("")) || (DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_Shop)) == 0)
                {
                    break;
                }

                contentValues.put(DBHelper.KEY_Salary, salary);
                contentValues.put(DBHelper.KEY_NameWorker, name);
                contentValues.put(DBHelper.KEY_Department, depart);
                database.update(DBHelper.TABLE_Shop, contentValues, DBHelper.KEY_Id + "= ?", new String[] {id});

                check = 0;
                readData.performClick();
                toast = Toast.makeText(getApplicationContext(),
                        "Updates rows with id = " + inputId.getText(), Toast.LENGTH_SHORT);
                toast.show();

                inputName.setText("");
                inputDepartment.setText("");
                inputSalary.setText("");
                inputId.setText("");
                break;
        }

        switch (v.getId())
        {
            case R.id.sortId:
                check = 0;
                if(checkSort[0] == 1)
                {
                    sortId.setText("Name ↓");
                    orderBy = "name desc";
                    checkSort[0] = 0;
                }else
                {
                    sortId.setText("Name ↑");
                    orderBy = "name";
                    checkSort[0]++;
                }
                readData.performClick();
                break;

            case R.id.sortDepart:
                check = 0;
                if(checkSort[1] == 1)
                {
                    sortDepart.setText("Depart ↓");
                    orderBy = "depart desc";
                    checkSort[1] = 0;
                }else
                {
                    sortDepart.setText("Depart ↑");
                    orderBy = "depart";
                    checkSort[1]++;
                }
                readData.performClick();
                break;

            case R.id.sortSalary:
                check = 0;
                if(checkSort[2] == 1)
                {
                    sortSalary.setText("Salary ↓");
                    orderBy = "salary desc";
                    checkSort[2] = 0;
                }else
                {
                    sortSalary.setText("Salary ↑");
                    orderBy = "salary";
                    checkSort[2]++;
                }
                readData.performClick();
                break;
        }
        dbHelper.close();
    }

}

