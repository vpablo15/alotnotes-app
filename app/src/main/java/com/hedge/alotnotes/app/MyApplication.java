package com.hedge.alotnotes.app;

import android.app.Application;

import com.hedge.alotnotes.models.Board;
import com.hedge.alotnotes.models.Note;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger boardID = new AtomicInteger();
    public static AtomicInteger noteID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        //Configurando Realm
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        Realm realm = Realm.getDefaultInstance();
        boardID = getIdByTable(realm, Board.class);
        noteID = getIdByTable(realm, Note.class);
        realm.close();
    }

    //Configurando el incrementador
    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm,Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }

}
