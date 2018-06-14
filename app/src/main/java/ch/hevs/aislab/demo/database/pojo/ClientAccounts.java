package ch.hevs.aislab.demo.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
public class ClientAccounts {
    @Embedded
    public ClientEntity client;

    @Relation(parentColumn = "email", entityColumn = "owner", entity = AccountEntity.class)
    public List<AccountEntity> accounts;
}