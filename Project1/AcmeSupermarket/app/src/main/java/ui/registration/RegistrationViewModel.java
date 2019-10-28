package ui.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<RegistrationState> registrationState
            = new MutableLiveData<>(RegistrationState.COLLECT_PROFILE_DATA);
    // Simulation of real-world scenario, where an auth token may be provided as
    // an alternate authentication mechanism instead of passing the password
    // around. This is set at the end of the registration process.
    private String authToken;

    public MutableLiveData<RegistrationState> getRegistrationState() {
        return registrationState;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void collectProfileData(String name, String username, String password) {
        // ... validate and store data
        // Change State to collecting username and password
        registrationState.setValue(RegistrationState.COLLECT_CREDIT_CARD_DATA);

    }

    public void createCreditCard(String cardNumber, String cardHolder, String cardExpiry, String cardCvv) {
        // ... create account
        // ... authenticate
        this.authToken = "oi";// token

        // Change State to registration completed
        registrationState.setValue(RegistrationState.REGISTRATION_COMPLETED);

    }

    public boolean userCancelledRegistration() {
        // Clear existing registration data
        registrationState.setValue(RegistrationState.COLLECT_PROFILE_DATA);
        authToken = "";
        return true;
    }


    enum RegistrationState {
        COLLECT_PROFILE_DATA,
        COLLECT_CREDIT_CARD_DATA,
        REGISTRATION_COMPLETED
    }

}
