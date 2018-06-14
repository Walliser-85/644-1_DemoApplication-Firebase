package ch.hevs.aislab.demo.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientAccounts;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public interface ClientDao {

    @Query("SELECT * FROM clients WHERE email = :id")
    LiveData<ClientEntity> getById(String id);

    @Query("SELECT * FROM clients")
    LiveData<List<ClientEntity>> getAll();

    /**
     * This method is used to populate the transaction activity.
     * It returns all OTHER users and their accounts.
     * @param id Id of the client who should be excluded from the list.
     * @return A live data object containing a list of ClientAccounts with
     * containing all clients but the @id.
     */
    @Transaction
    @Query("SELECT * FROM clients WHERE email != :id")
    LiveData<List<ClientAccounts>> getOtherClientsWithAccounts(String id);

    @Insert
    long insert(ClientEntity client) throws SQLiteConstraintException;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ClientEntity> clients);

    @Update
    void update(ClientEntity client);

    @Delete
    void delete(ClientEntity client);

    @Query("DELETE FROM clients")
    void deleteAll();
}
