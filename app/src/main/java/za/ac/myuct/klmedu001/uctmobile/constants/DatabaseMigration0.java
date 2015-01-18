package za.ac.myuct.klmedu001.uctmobile.constants;

import android.database.sqlite.SQLiteDatabase;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;

/**
 * Created by eduardokolomajr on 2014/12/03.
 * Initial Migration
 */
@Migration(version = AppDatabase.VERSION, databaseName = AppDatabase.NAME)
public class DatabaseMigration0 extends BaseMigration {
    @Override
    public void onPreMigrate() {
            super.onPreMigrate();
    }

    @Override
    public void migrate(SQLiteDatabase sqLiteDatabase) {

    }
}
