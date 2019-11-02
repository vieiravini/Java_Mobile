package br.com.fatecararas.persistenciadedados;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import br.com.fatecararas.persistenciadedados.adapter.SQLiteAdapter;
import br.com.fatecararas.persistenciadedados.helper.MySQLiteOpenHelper;
import br.com.fatecararas.persistenciadedados.model.Dicionario;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText mEditTextPalavra;
    EditText mEditTextDefinicao;
    MySQLiteOpenHelper mDB;
    ListView mListView;
    List<Dicionario> mDataSource = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditTextPalavra = findViewById(R.id.TextInputPalavra);
        mEditTextDefinicao = findViewById(R.id.TextInputSignificado);
        mListView = findViewById(R.id.listViewDicionario);

        mDB = new MySQLiteOpenHelper(this);

        exibirDadosEmLista();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecord();
                Snackbar.make(view, "Registro adicionado com sucesso!", Snackbar.LENGTH_LONG).show();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Position/ID: ",position + " - "+id);
                Toast.makeText(MainActivity.this,
                        mDB.buscarDefinicaoPalavra(id).toString(),
                        Toast.LENGTH_SHORT).show();
            } });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                        "Records deleted = " + mDB.apagarRegistro(id),Toast.LENGTH_SHORT).show();
                exibirDadosEmLista();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void exibirDadosEmLista(){
        mDataSource.clear();

        SQLiteDatabase sqLiteDatabase = mDB.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM dicionario",null);

        if (cursor.moveToFirst()){
            do {
                Dicionario dicionario = new Dicionario();
                dicionario.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                dicionario.setPalavra(cursor.getString(cursor.getColumnIndex("palavra")));
                dicionario.setDefinicao(cursor.getString(cursor.getColumnIndex("definicao")));
                mDataSource.add(dicionario);
            }while (cursor.moveToNext());
        }

        SQLiteAdapter sqLiteAdapter =
                new SQLiteAdapter(MainActivity.this, mDataSource);
        sqLiteAdapter.notifyDataSetChanged();
        mListView.setAdapter(sqLiteAdapter);
        cursor.close();
    }

    private void saveRecord() {
        String palavra = mEditTextPalavra.getText().toString();
        String definicao = mEditTextDefinicao.getText().toString();
        mDB.salvarRegistro(palavra, definicao);
        mEditTextPalavra.setText("");
        mEditTextDefinicao.setText("");
        exibirDadosEmLista();
    }



}
