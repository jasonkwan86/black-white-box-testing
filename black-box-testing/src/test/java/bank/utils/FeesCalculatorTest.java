package bank.utils;

import org.junit.jupiter.api.*;


import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeesCalculatorTest {
    private final FeesCalculator calculator = new FeesCalculator();
    private static final double WITHDRAWAL_AMOUNT = 100.0;
    private static final double DELTA = 0.001;

    @Nested
    @DisplayName("withdrawal fee tests")
    class WithdrawalTests {

        @Test
        @DisplayName("test 1: student on a weekday should have NO fee")
        void student_weekday_no_fee() {
            // should fail, code charges but spec said it shouldnt
            double expectedFee = 0.00;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, true, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 2: Student on a weekend should have 0.1% fee")
        void student_weekend_has_fee() {
            // should fail, code doesnt charge but spec said it should
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001; // 0.1%
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, true, Calendar.SATURDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 3: non-student with balance -500.00 should have 0.3% fee")
        void non_student_balance_negative_is_point_three_percent_fee() {
            // should fail, spec says 0.3%, code says 0.2%
            double expectedFee = WITHDRAWAL_AMOUNT * 0.003;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, -500.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 4: non-student with balance 0.00 should have 0.3% fee")
        void non_student_balance_zero_is_point_three_percent_fee() {
            // should fail, spec says 0.3%, code says 0.2%
            double expectedFee = WITHDRAWAL_AMOUNT * 0.003;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 0.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 5: non-student with balance 999.99 should have 0.3% fee")
        void non_student_balance_just_below_1000_is_point_three_percent_fee() {
            // should fail, spec says 0.3%, code says 0.2%
            double expectedFee = WITHDRAWAL_AMOUNT * 0.003;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 999.99, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 6: non-student with balance 1000.00 should have 0.1% fee")
        void non_student_balance_at_1000_is_point_one_percent_fee() {
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 1000.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 7: non-student with balance 4999.99 should have 0.1% fee")
        void non_student_balance_just_below_5000_is_point_one_percent_fee() {
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 4999.99, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 8: non-student with balance 5000.00 should have 0.1% fee per spec")
        void non_student_balance_at_5000_is_point_one_percent_fee() {
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 5000.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 9: non-student with balance 5000.01 should have NO fee per spec")
        void non_student_balance_just_above_5000_is_no_fee() {
            // should fail, spec says no fee over 5000, but code charges 0.1%
            double expectedFee = 0.00;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 5000.01, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 10: non-student with balance 7500.00 (nominal) should have NO fee per spec")
        void non_student_balance_nominal_above_5000_is_no_fee() {
            // should fail, spec says no fee, but code charges 0.1% as it's below 10000
            double expectedFee = 0.00;
            double actualFee = calculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 7500.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }
    }

    @Nested
    class DepositTests
    {
        @Test
        @DisplayName("Test 1: Deposit below 0")
        void depositInterestTest_StudentDepositBelow0() {
            // shouldn't be able to deposit negative amounts
            // in real life interest rate should always be positive
            double result = calculator.calculateDepositInterest(-1, 100, true);
            assertTrue(result > 0);
        }

        @Test
        @DisplayName("Test 2: Deposit Equal to 0")
        void depositInterestTest_StudentDepositEqual0() {
            assertEquals(0, calculator.calculateDepositInterest(0, 100, true));
        }

        @Test
        @DisplayName("Test 3: Deposit with balance below 0")
        void depositInterestTest_StudentDepositBalanceBelow0() {
            // shouldn't be able to deposit negative amounts
            // in real life interest rate should always be positive
            double result = calculator.calculateDepositInterest(1, -1, true);
            assertTrue(result > 0);
        }

        @Test
        @DisplayName("Test 4: Deposit with balance equal to 0")
        void depositInterestTest_StudentDepositBalanceEqual0() {
            assertEquals(0, calculator.calculateDepositInterest(1, 0, true));
        }

        @Test
        @DisplayName("Test 5: Expected interest rate of 0.5%")
        void depositInterestTest_StudentDepositAbove50BalanceAbove500() {
            double amount = 51;
            double expectedInterest = amount * 0.005;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 501, true));
        }

        @Test
        @DisplayName("Test 6: Expected interest rate of 0.25%")
        void depositInterestTest_StudentDepositAbove50BalanceNotAbove500() {
            double amount = 51;
            double expectedInterest = amount * 0.0025;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 499, true));
        }

        @Test
        @DisplayName("Test 7: Expected interest rate of 0.5%")
        void depositInterestTest_StudentDepositNotAbove50BalanceAbove5000() {
            double amount = 49;
            double expectedInterest = amount * 0.005;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 5001, true));
        }

        @Test
        @DisplayName("Test 8: Expected interest rate of 0%")
        void depositInterestTest_StudentDepositNotAbove50BalanceNotAbove5000() {
            assertEquals(0, calculator.calculateDepositInterest(49, 4999, true));
        }

        @Test
        @DisplayName("Test 9: Expected interest rate of 0.8%")
        void depositInterestTest_NotStudentDepositAbove250BalanceAbove2500() {
            double amount = 251;
            double expectedInterest = amount * 0.008;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 2501, false));
        }

        @Test
        @DisplayName("Test 10: Expected interest rate of 0.4%")
        void depositInterestTest_NotStudentDepositAbove250BalanceNotAbove2500() {
            double amount = 251;
            double expectedInterest = amount * 0.004;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 2499, false));
        }

        @Test
        @DisplayName("Test 11: Expected interest rate of 0%")
        void depositInterestTest_NotStudentDepositNotAbove250BalanceAbove10000() {
            double amount = 249;
            assertEquals(0, calculator.calculateDepositInterest(amount, 10001, false));
        }

        @Test
        @DisplayName("Test 12: Expected interest rate of 0.1%")
        void depositInterestTest_NotStudentDepositNotAbove250BalanceNotAbove10000() {
            double amount = 249;
            double expectedInterest = amount * 0.001;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 9999, false));
        }
    }

    @Nested
    class TransferTests
    {
        @Test
        @DisplayName("Case #1, expected fee of 0.1%")
        void transferTest_StudentTranferUnder200BalanceSourceUnder2000BalanceDestinationUnder1000()
        {
            double transferAmount = 199;
            double expectedFee = transferAmount * 0.001;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 1999, 999, true));
        }

        @Test
        @DisplayName("Case #2, expected fee of 0.05%")
        void transferTest_StudentTransferUnder200BalanceSourceUnder2000BalanceDestinationNotUnder1000()
        {
            double transferAmount = 199;
            double expectedFee = transferAmount * 0.0005;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 1999, 1001, true));
        }

        @Test
        @DisplayName("Case #3, expected fee of 0.05%")
        void transferTest_StudentTransferUnder200BalanceSourceBetween2000And4000BalanceDestinationUnder1000()
        {
            double transferAmount = 199;
            double expectedFee = transferAmount * 0.0005;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 2001, 999, true));
        }

        @Test
        @DisplayName("Case #4, expected fee of 0.025%")
        void transferTest_StudentTransferUnder200BalanceSourceNotBelow2000BalanceDestinationNotUnder1000()
        {
            double transferAmount = 199;
            double expectedFee = transferAmount * 0.00025;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 2001, 1001, true));
        }

        @Test
        @DisplayName("Case #5, expected fee of 0.05%")
        void transferTest_StudentTransferNotUnder200BalanceSourceNotBelow2000BalanceDestinationUnder1000()
        {
            double transferAmount = 201;
            double expectedFee = transferAmount * 0.0005;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 2001, 999, true));
        }

        @Test
        @DisplayName("Case #6, expected fee of 0.025%")
        void transferTest_StudentTransferNotUnder200BalanceSourceBelow2000BalanceDestinationNotUnder1000()
        {
            double transferAmount = 201;
            double expectedFee = transferAmount * 0.0005;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 1999, 1001, true));
        }

        @Test
        @DisplayName("Case #7, expected fee of 0.25%")
        void transferTest_StudentTransferNotUnder200BalanceSourceBelow2000BalanceDestinationUnder1000()
        {
            double transferAmount = 201;
            double expectedFee = transferAmount * 0.0025;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 1999, 999, true));
        }

        @Test
        @DisplayName("Case #8, expected fee of 0.125%")
        void transferTest_StudentTransferNotUnder200BalanceSourceNotBelow2000BalanceDestinationNotUnder1000()
        {
            double transferAmount = 201;
            double expectedFee = transferAmount * 0.00125;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 2001, 1001, true));
        }

        @Test
        @DisplayName("Case #9, expected fee of 0.2%")
        void transferTest_NotStudentTransferUnder100BalanceSourceUnder4000BalanceDestinationUnder2000()
        {
            double transferAmount = 99;
            double expectedFee = transferAmount * 0.002;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 3999, 1999, false));
        }

        @Test
        @DisplayName("Case #10, expected fee of 0.1%")
        void transferTest_NotStudentTransferUnder100BalanceSourceUnder4000BalanceDestinationNotUnder2000()
        {
            double transferAmount = 99;
            double expectedFee = transferAmount * 0.001;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 3999, 2001, false));
        }

        @Test
        @DisplayName("Case #11, expected fee of 1%")
        void transferTest_NotStudentTransferUnder100BalanceSourceNotUnder4000BalanceDestinationUnder2000()
        {
            double transferAmount = 99;
            double expectedFee = transferAmount * 0.001;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 4001, 1999, false));
        }

        @Test
        @DisplayName("Case #12, expected fee of 0.5%")
        void transferTest_NotStudentTransferUnder100BalanceSourceNotUnder4000BalanceDestinationNotUnder2000()
        {
            double transferAmount = 99;
            double expectedFee = transferAmount * 0.005;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 4001, 2001, false));
        }

        @Test
        @DisplayName("Case #13, expected fee of 0.2%")
        void transferTest_NotStudentTransferNotUnder100BalanceSourceUnder2000BalanceDestinationUnder1000()
        {
            double transferAmount = 101;
            double expectedFee = transferAmount * 0.002;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 1999, 999, false));
        }

        @Test
        @DisplayName("Case #14, expected fee of 0.1%")
        void transferTest_NotStudentTransferNotUnder100BalanceSourceUnder2000BalanceDestinationNotUnder1000()
        {
            double transferAmount = 101;
            double expectedFee = transferAmount * 0.001;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 1999, 1001, false));
        }

        @Test
        @DisplayName("Case #15, expected fee of 0.5%")
        void transferTest_NotStudentTransferNotUnder100BalanceSourceNotUnder2000BalanceDestinationUnder1000()
        {
            double transferAmount = 101;
            double expectedFee = transferAmount * 0.005;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 2001, 999, false));
        }

        @Test
        @DisplayName("Case #16, expected fee of 0.25%")
        void transferTest_NotStudentTransferNotUnder100BalanceSourceNotUnder2000BalanceDestinationNotUnder1000()
        {
            double transferAmount = 101;
            double expectedFee = transferAmount * 0.0025;
            assertEquals(expectedFee, calculator.calculateTransferFee(transferAmount, 2001, 1001, false));
        }
    }
}
