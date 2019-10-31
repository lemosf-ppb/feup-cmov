package ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import models.Client;

public class LoginViewModel extends ViewModel {

    public final MutableLiveData<AuthenticationState> authenticationState =
            new MutableLiveData<>();

    private Client client;

    public LoginViewModel() {
        // In this example, the user is always unauthenticated when MainActivity is launched
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
        this.client = null;
    }

    public void authenticate(Client client) {
        this.client = client;
        authenticationState.setValue(AuthenticationState.AUTHENTICATED);
    }

    public void authenticate(String username, String password) {
        if (passwordIsValidForUsername(username, password)) {
//            this.username = username;
            authenticationState.setValue(AuthenticationState.AUTHENTICATED);
        } else {
            authenticationState.setValue(AuthenticationState.INVALID_AUTHENTICATION);
        }
    }

    public void refuseAuthentication() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    private boolean passwordIsValidForUsername(String username, String password) {
        return true;
    }

    public Client getClient() {
        return client;
    }

    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }
}
