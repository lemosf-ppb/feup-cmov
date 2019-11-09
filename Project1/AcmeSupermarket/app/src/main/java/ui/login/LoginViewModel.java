package ui.login;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import models.Client;
import services.repository.AcmeRepository;
import utils.Utils;

public class LoginViewModel extends ViewModel {
    private static final String CLIENT_FILENAME = "clientData";
    private static final String TAG = "LoginViewModel";

    public final MutableLiveData<AuthenticationState> authenticationState =
            new MutableLiveData<>();

    public final MutableLiveData<Client> client = new MutableLiveData<>();

    public LoginViewModel() {
        // The user is always unauthenticated when MainActivity is launched
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
        this.client.setValue(null);
    }

    public void authenticate(Client client, Context context) {
        this.client.setValue(client);
        saveClient(client, context);
        authenticationState.setValue(AuthenticationState.AUTHENTICATED);
    }

    public void authenticate(String username, String password, Context context) {
        Client client = loadClient(username, context);
        if (client != null && passwordIsValidForUsername(username, password, client)) {
            this.client.setValue(client);
            authenticationState.setValue(AuthenticationState.AUTHENTICATED);
        } else {
            AcmeRepository.LogIn logIn = new AcmeRepository.LogIn(this, username, password, context);
            new Thread(logIn).start();
        }
    }

    public void refuseAuthentication() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    private boolean passwordIsValidForUsername(String username, String password, Client client) {
        return client.getUsername().equals(username) && client.getPassword().equals(password);
    }

    public void saveClient(Client client, Context context) {
        Utils.saveObject(client.getUsername() + "_" + CLIENT_FILENAME, client, context);
    }

    private Client loadClient(String username, Context context) {
        return (Client) Utils.loadObject(username + "_" + CLIENT_FILENAME, context);
    }

    public Client getClient() {
        return client.getValue();
    }

    public void syncDatabase() {
        AcmeRepository.getUserInfo getUserInfo = new AcmeRepository.getUserInfo(this);
        new Thread(getUserInfo).start();
    }

    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }
}
