package ui.registration;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import models.Client;
import models.CreditCard;
import services.crypto.CryptoKeysManagement;
import services.repository.AbstractRestCall;
import services.repository.AcmeRepository;

public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<RegistrationState> registrationState
            = new MutableLiveData<>(RegistrationState.COLLECT_PROFILE_DATA);
    private final MutableLiveData<AbstractRestCall.Response> response = new MutableLiveData<>();
    private Client client;

    public MutableLiveData<RegistrationState> getRegistrationState() {
        return registrationState;
    }

    public Client getClient() {
        return client;
    }

    public MutableLiveData<AbstractRestCall.Response> getSignUpResponse() {
        return response;
    }

    public void collectProfileData(String name, String username, String password) {
        // Store profile data
        client = new Client(name, username, password);

        // Change State to collecting username and password
        registrationState.setValue(RegistrationState.COLLECT_CREDIT_CARD_DATA);
    }

    public void createCreditCard(String cardNumber, String cardHolder, String cardExpiry, String cardCvv) {
        // Store Credit Card
        CreditCard creditCard = new CreditCard(cardHolder, cardNumber, cardExpiry, cardCvv);
        client.setCreditCard(creditCard);
    }

    public void userCancelledRegistration() {
        // Clear existing registration data
        registrationState.setValue(RegistrationState.COLLECT_PROFILE_DATA);
    }

    public void signUp(Context context) {
        CryptoKeysManagement.generateAndStoreKeys(context);
        client.setClientKeys();

        AcmeRepository.SignUp signUp = new AcmeRepository.SignUp(this);
        new Thread(signUp).start();
    }

    public enum RegistrationState {
        COLLECT_PROFILE_DATA,
        COLLECT_CREDIT_CARD_DATA,
        REGISTRATION_COMPLETED,
        REGISTRATION_FAILED
    }
}
