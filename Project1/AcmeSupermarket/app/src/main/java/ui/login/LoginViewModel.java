package ui.login;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import models.Client;
import services.repository.AcmeRepository;
import ui.shop.ShopViewModel;

public class LoginViewModel extends ViewModel {

    public final MutableLiveData<AuthenticationState> authenticationState =
            new MutableLiveData<>();
    private final String fileName = "clientData";

    private Client client;

    public LoginViewModel() {
        // In this example, the user is always unauthenticated when MainActivity is launched
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
        this.client = null;
    }

    public void authenticate(Client client, Context context) {
        this.client = client;
        saveClient(client, context);
        authenticationState.setValue(AuthenticationState.AUTHENTICATED);
    }

    public void authenticate(String username, String password, Context context) {
        Client client = loadClient(context);
        if (client != null && passwordIsValidForUsername(username, password, client)) {
            this.client = client;
            authenticationState.setValue(AuthenticationState.AUTHENTICATED);
        } else {
            authenticationState.setValue(AuthenticationState.INVALID_AUTHENTICATION);
        }
    }

    public void refuseAuthentication() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    private boolean passwordIsValidForUsername(String username, String password, Client client) {
        return client.getUsername().equals(username) && client.getPassword().equals(password);
    }

    private void saveClient(Client client, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            os.writeObject(client);

            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client loadClient(Context context) {
        Client client = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);

            client = (Client) is.readObject();

            is.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return client;
    }

    public Client getClient() {
        return client;
    }

    public void syncDatabase(ShopViewModel shopViewModel) {
        AcmeRepository.getUserInfo getUserInfo = new AcmeRepository.getUserInfo(this, shopViewModel);
        new Thread(getUserInfo).start();
    }

    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }
}
