package atm;

import atm.dispatcher.MessageDispatcher;
import atm.exceptions.InvalidAmountException;
import atm.exceptions.InvalidCredentialsException;
import atm.exceptions.InvalidPinFormatException;
import atm.ui.panels.MainPanel;
import atm.utils.CashValidator;
import atm.utils.CredentialsCheck;
import atm.utils.FormatChecker;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ATMTest {
    ATM atm;
    MessageDispatcher dispatcher;


    @BeforeEach
    public void setUp() throws Exception {
        dispatcher = Mockito.mock(MessageDispatcher.class);
        FormatChecker formatCheck = new FormatChecker();
        CredentialsCheck credentialsCheck = new CredentialsCheck(dispatcher);
        atm = new ATM(formatCheck, credentialsCheck, dispatcher);

        MainPanel mainPanel = Mockito.mock(MainPanel.class);
        atm.setMainPanel(mainPanel);
        atm.createSession();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void checkCorrectPINTest() {
        // This test mocks a correct credential check for a 4-digit PIN
        Mockito.when(dispatcher.checkCredentials(null, new char[]{'5', '5', '5', '5'})).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> atm.checkPin(new char[]{'5', '5', '5', '5'}));
    }

    @Test
    public void checkIncorrectPINTest() {
        // This test mocks a failed credential check
        Mockito.when(dispatcher.checkCredentials(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false);
        Assertions.assertThrows(InvalidCredentialsException.class, () -> atm.checkPin(new char[]{'5', '5', '5', '4'}));
    }


    @Nested
    @DisplayName("pin format validation tests (need 5 digits)")
    class PinFormatTests {
        private FormatChecker formatChecker = new FormatChecker();

        @Test
        @DisplayName("test 1: pins with 4 digits should throw exception")
        void pin_length_4_throws_exception() {
            char[] pin = "1234".toCharArray();
            // should fail, expects 4 but spec needs 5
            assertThrows(InvalidPinFormatException.class, () -> formatChecker.checkPinFormat(pin),
                    "Spec requires 5-digit PIN, so 4 digits should be invalid.");
        }

        @Test
        @DisplayName("test 2: pins with 5 digits should pass")
        void pin_length_5_is_valid() {
            char[] pin = "12345".toCharArray();
            // should fail, implemented code only accepts 4 digit pins
            assertDoesNotThrow(() -> formatChecker.checkPinFormat(pin),
                    "Spec requires 5-digit PIN, so this should be valid.");
        }

        @Test
        @DisplayName("test 3: pins with 6 digits should throw exception")
        void pin_length_6_throws_exception() {
            char[] pin = "123456".toCharArray();
            assertThrows(InvalidPinFormatException.class, () -> formatChecker.checkPinFormat(pin));
        }

        @Test
        @DisplayName("test 4: pins with non-numeric character throws exception")
        void pin_with_alpha_char_throws_exception() {
            char[] pin = "1234a".toCharArray();
            // code doesnt check content, should fail and not throw the correct exception
            assertThrows(InvalidPinFormatException.class, () -> formatChecker.checkPinFormat(pin));
        }

        @Test
        @DisplayName("test 5: empty pin should throw exception")
        void pin_empty_throws_exception() {
            char[] pin = "".toCharArray();
            assertThrows(InvalidPinFormatException.class, () -> formatChecker.checkPinFormat(pin));
        }

        @Test
        @DisplayName("test 6: null pin should throw exception")
        void pin_null_throws_exception() {
            // This will likely throw a NullPointerException, which is still a failure
            assertThrows(Exception.class, () -> formatChecker.checkPinFormat(null));
        }
    }

    @Nested
    @DisplayName("withdrawal amount validation tests (multiple of 20, range of $20 to $1000)")
    class AmountValidationTests {

        @Test
        @DisplayName("test 1: amount -20 should be invalid")
        void amount_negative_20_is_invalid() {
            assertFalse(CashValidator.validateWithdrawal(-20));
        }

        @Test
        @DisplayName("test 2: amount 0 should be invalid")
        void amount_0_is_invalid() {
            // should fail, code considers 0 valid for some reason
            assertFalse(CashValidator.validateWithdrawal(0));
        }

        @Test
        @DisplayName("test 3: amount 19 (min-1) should be invalid")
        void amount_19_is_invalid() {
            assertFalse(CashValidator.validateWithdrawal(19));
        }

        @Test
        @DisplayName("test 4: amount 20 (min) should be valid")
        void amount_20_is_valid() {
            assertTrue(CashValidator.validateWithdrawal(20));
        }

        @Test
        @DisplayName("test 5: amount 21 (min+1) should be invalid")
        void amount_21_is_invalid() {
            assertFalse(CashValidator.validateWithdrawal(21));
        }

        @Test
        @DisplayName("test 6: amount 500 (nominal) should be valid")
        void amount_500_is_valid() {
            assertTrue(CashValidator.validateWithdrawal(500));
        }

        @Test
        @DisplayName("test 7: amount 999 (max-1) should be invalid")
        void amount_999_is_invalid() {
            assertFalse(CashValidator.validateWithdrawal(999));
        }

        @Test
        @DisplayName("test 8: amount 1000 (max) should be valid")
        void amount_1000_is_valid() {
            assertTrue(CashValidator.validateWithdrawal(1000));
        }

        @Test
        @DisplayName("test 9: amount 1001 (max+1) should be invalid")
        void amount_1001_is_invalid() {
            assertFalse(CashValidator.validateWithdrawal(1001));
        }

        @Test
        @DisplayName("test 10: Amount 60 should be valid but fails due to code bug")
        void amount_60_is_valid_by_spec() {
            // should fail, code in cashvalidator is wrong
            assertTrue(CashValidator.validateWithdrawal(60));
        }
    }
}