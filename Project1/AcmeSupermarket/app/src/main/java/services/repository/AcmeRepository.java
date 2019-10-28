package services.repository;

public class AcmeRepository{
    public class SignUp extends AbstractRestCall {

        public SignUp() {
        }

        @Override
        public String createPayload() {
            return null;
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class LogIn extends AbstractRestCall {

        @Override
        public String createPayload() {
            return null;
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class getTransactions extends AbstractRestCall {

        @Override
        public String createPayload() {
            return null;
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class getUnusedVouchers extends AbstractRestCall {

        @Override
        public String createPayload() {
            return null;
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class getUserInfo extends AbstractRestCall {

        @Override
        public String createPayload() {
            return null;
        }

        @Override
        public void handleResponse(String response) {

        }
    }
}
