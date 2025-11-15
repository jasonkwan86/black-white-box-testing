package bank.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

// Use of Parameterized helps in this case, since multiple runs of same test are required
class FeesCalculatorTest {
	FeesCalculator calculator = new FeesCalculator();

    @Nested
    class DepositTests
    {
        @Test
        void depositInterestTest_StudentDepositBelow0() {
            // shouldn't be able to deposit negative amounts
            // in real life interest rate should always be positive
            double result = calculator.calculateDepositInterest(-1, 100, true);
            assertTrue(result > 0);
        }

        @Test
        void depositInterestTest_StudentDepositEqual0() {
            assertEquals(0, calculator.calculateDepositInterest(0, 100, true));
        }

        @Test
        void depositInterestTest_StudentDepositBalanceBelow0() {
            // shouldn't be able to deposit negative amounts
            // in real life interest rate should always be positive
            double result = calculator.calculateDepositInterest(1, -1, true);
            assertTrue(result > 0);
        }

        @Test
        void depositInterestTest_StudentDepositBalanceEqual0() {
            assertEquals(0.0, calculator.calculateDepositInterest(1, 0, true));
        }

        @Test
        void depositInterestTest_StudentDepositAbove50BalanceAbove500() {
            assertEquals(0.005, calculator.calculateDepositInterest(51, 501, true));
        }

        @Test
        void depositInterestTest_StudentDepositAbove50BalanceNotAbove500() {
            assertEquals(0.0025, calculator.calculateDepositInterest(51, 500, true));
        }

        @Test
        void depositInterestTest_StudentDepositNotAbove50BalanceAbove5000() {
            assertEquals(0.005, calculator.calculateDepositInterest(50, 5001, true));
        }

        @Test
        void depositInterestTest_StudentDepositNotAbove50BalanceNotAbove5000() {
            assertEquals(0, calculator.calculateDepositInterest(50, 5000, true));
        }

        @Test
        void depositInterestTest_NotStudentDepositAbove250BalanceAbove2500() {
            double amount = 251;
            double expectedInterest = amount * 0.008;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 2501, false));
        }

        @Test
        void depositInterestTest_NotStudentDepositAbove250BalanceNotAbove2500() {
            double amount = 251;
            double expectedInterest = amount * 0.004;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 2500, false));
        }

        @Test
        void depositInterestTest_NotStudentDepositNotAbove250BalanceAbove10000() {
            double amount = 250;
            assertEquals(0, calculator.calculateDepositInterest(amount, 10001, false));
        }

        @Test
        void depositInterestTest_NotStudentDepositNotAbove250BalanceNotAbove10000() {
            double amount = 250;
            double expectedInterest = amount * 0.001;
            assertEquals(expectedInterest, calculator.calculateDepositInterest(amount, 10000, false));
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
