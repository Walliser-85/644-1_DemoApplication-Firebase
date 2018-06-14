package ch.hevs.aislab.demo.database;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

/**
 * Generates dummy data
 */
public class DataGenerator {

    public static List<ClientEntity> generateClients() {
        List<ClientEntity> clients = new ArrayList<>();

        ClientEntity client1 = new ClientEntity();
        client1.setFirstName("Michel");
        client1.setLastName("Platini");
        client1.setEmail("m.p@fifa.com");
        client1.setPassword("michel1");

        ClientEntity client2 = new ClientEntity();
        client2.setFirstName("Sepp");
        client2.setLastName("Blatter");
        client2.setEmail("s.b@fifa.com");
        client2.setPassword("sepp1");

        ClientEntity client3 = new ClientEntity();
        client3.setFirstName("Ebbe");
        client3.setLastName("Schwartz");
        client3.setEmail("e.s@fifa.com");
        client3.setPassword("ebbe1");

        ClientEntity client4 = new ClientEntity();
        client4.setFirstName("Aleksander");
        client4.setLastName("Ceferin");
        client4.setEmail("a.c@fifa.com");
        client4.setPassword("aleksander1");

        clients.add(client1);
        clients.add(client2);
        clients.add(client3);
        clients.add(client4);

        return clients;
    }

    public static List<AccountEntity> generateAccountsForClients(List<ClientEntity> clients) {
        List<AccountEntity> accounts = new ArrayList<>();

        AccountEntity account1 = new AccountEntity();
        AccountEntity account2 = new AccountEntity();
        AccountEntity account3 = new AccountEntity();
        AccountEntity account4 = new AccountEntity();
        AccountEntity account5 = new AccountEntity();
        AccountEntity account6 = new AccountEntity();
        AccountEntity account7 = new AccountEntity();
        AccountEntity account8 = new AccountEntity();

        account1.setBalance(20000d);
        account1.setName("Savings");
        account1.setOwner(clients.get(0).getEmail());
        accounts.add(account1 );

        account2.setBalance(1840000d);
        account2.setName("Secret");
        account2.setOwner(clients.get(0).getEmail());
        accounts.add(account2);

        account3.setBalance(21000d);
        account3.setName("Savings");
        account3.setOwner(clients.get(1).getEmail());
        accounts.add(account3);

        account4.setBalance(1820000d);
        account4.setName("Secret");
        account4.setOwner(clients.get(1).getEmail());
        accounts.add(account4);

        account5.setBalance(18500d);
        account5.setName("Savings");
        account5.setOwner(clients.get(2).getEmail());
        accounts.add(account5);

        account6.setBalance(1810000d);
        account6.setName("Secret");
        account6.setOwner(clients.get(2).getEmail());
        accounts.add(account6);

        account7.setBalance(19000d);
        account7.setName("Savings");
        account7.setOwner(clients.get(3).getEmail());
        accounts.add(account7);

        account8.setBalance(1902360d);
        account8.setName("Secret");
        account8.setOwner(clients.get(3).getEmail());
        accounts.add(account8);

        return accounts;
    }
}
