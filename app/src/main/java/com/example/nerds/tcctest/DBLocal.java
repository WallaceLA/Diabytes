package com.example.nerds.tcctest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
/* IMPORTANTE: ESSE APP SÓ FUNCIONOU DO ANDROID 5.0 API 23 PARA CIMA!!*/
public class DBLocal extends SQLiteOpenHelper{

    //Variáveis final para uso no DB
    private static final String DB = "mydb";
    private static final String TABLE = "alimento";
    //Variáveis final para uso como referência nos inserts
    private static final String NOME = "nome";
    private static final String PORCAO = "porcao";
    private static final String CARB = "gcarb";
    private static final String TAG = "DBLocal";


    public DBLocal(Context context) {
        /* Normalmente, os parâmetros são context, name, factory e version.
        * Porém, nesse caso, só será necessário usar o context como parâmetro, sendo assim:*/
        super(context, DB, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Mais para fins de teste, eu concatenei o valor da tabela.
        db.execSQL("CREATE TABLE " + TABLE + "(id INTEGER PRIMARY KEY, nome TEXT, "
                +"porcao TEXT, gcarb REAL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertAlimento (Alimento alimento){

        SQLiteDatabase bd = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            //Tenta criar valores baseado no objeto Alimento
            contentValues.put(NOME, alimento.getNome());
            contentValues.put(PORCAO, alimento.getPorcao());
            contentValues.put(CARB, alimento.getgCarb());
            bd.insert(TABLE, null, contentValues); //Na ordem: tabela, TableHack (?), valores a serem add
            return true;
        }catch(Exception e){
            Log.e(TAG, "insertAlimento: " +e.getMessage());
            return false;
        }

    }

    public ArrayList<Alimento> selectAlimentos(){

        SQLiteDatabase bd = this.getReadableDatabase();
        ArrayList<Alimento> lista = new ArrayList<Alimento>();
        System.out.println(lista.size());
        try{
        Cursor cur = bd.rawQuery("SELECT * FROM " + TABLE, null);
        //Pega o primeiro resultado
        cur.moveToFirst();
        //Enquanto não for o último dado
        while(cur.isAfterLast() == false){
            //Informações sobre um alimento
            Alimento a = new Alimento();
            a.setId(cur.getInt(cur.getColumnIndex("id"))); //Pega o ID da coluna
            /* Seta de acordo com o cursor*/
            a.setNome(cur.getString(cur.getColumnIndex(NOME)));
            a.setPorcao(cur.getString(cur.getColumnIndex(PORCAO)));
            a.setgCarb(cur.getFloat(cur.getColumnIndex(CARB)));
            lista.add(a);
            cur.moveToNext(); //Após o fim das informações, move-se à próxima
        }
        return lista;
    }catch(Exception e){
            Log.e(TAG, "selectAlimentos: " + e.getMessage());
            return lista;
        }
    }
}
