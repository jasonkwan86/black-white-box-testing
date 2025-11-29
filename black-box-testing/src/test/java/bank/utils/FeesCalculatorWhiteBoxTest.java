package bank.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * white box tests for a2
 */
public class FeesCalculatorWhiteBoxTest {

    private FeesCalculator feesCalculator;
    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        feesCalculator = new FeesCalculator();
    }

    @Nested
    @DisplayName("withdrawal tests (slicing & statement coverage)")
    class WithdrawalTests {

        private static final double WITHDRAWAL_AMOUNT = 100.0;

        @Test
        @DisplayName("wdl_tc01: student, weekend path")
        void testStudentWeekendPath() {
            double expectedFeeFromSpec = 0.10;
            // spec needs: 0.1% fee = 0.10. actual code: 0.0% fee = 0.00.
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, true, Calendar.SATURDAY);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA, "test fails due to spec/code mismatch on student weekend fee.");
        }

        @Test
        @DisplayName("wdl_tc02: student, weekday path")
        void testStudentWeekdayPath() {
            double expectedFeeFromSpec = 0.00;
            // spec needs: 0.0% fee = 0.00. actual code: 0.1% fee = 0.10.
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, true, Calendar.MONDAY);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA, "test fails due to spec/code mismatch on student weekday fee.");
        }

        @Test
        @DisplayName("wdl_tc03: non-student, balance < 1000 path")
        void testNonStudentLowBalancePath() {
            double expectedFeeFromSpec = 0.30;
            // spec needs: 0.3% fee = 0.30. actual code: 0.2% fee = 0.20.
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, false, Calendar.MONDAY);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA, "test fails due to spec/code mismatch on non-student low balance fee.");
        }

        @Test
        @DisplayName("wdl_tc04: non-student, 1000 <= balance < 10000 path")
        void testNonStudentMidBalancePath() {
            double expectedFeeFromSpec = 0.10;
            // spec needs: 0.1% fee = 0.10. actual code: 0.1% fee = 0.10. this should pass.
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 5000.0, false, Calendar.MONDAY);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("wdl_tc05: non-student, balance >= 10000 path")
        void testNonStudentHighBalancePath() {
            double expectedFeeFromSpec = 0.00;
            // spec needs: no fee (>5000) = 0.00. actual code: no fee (>=10000) = 0.00. this should pass.
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 15000.0, false, Calendar.MONDAY);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }
    }

    @Nested
    @DisplayName("deposit tests (du-path coverage)")
    class DepositTests {

        @Test
        @DisplayName("dep_tc01: path s-a>100-b>1000")
        void testDepositPath1() {
            double expectedFeeFromSpec = 0.505;
            // spec needs: 0.5% fee = 0.505. actual code: 1.0% fee = 1.01.
            double actualFee = feesCalculator.calculateDepositInterest(101.0, 1001.0, true);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc02: path s-a>100-b<=1000")
        void testDepositPath2() {
            double expectedFeeFromSpec = 0.2525;
            // spec needs: 0.25% fee = 0.2525. actual code: 0.5% fee = 0.505.
            double actualFee = feesCalculator.calculateDepositInterest(101.0, 500.0, true);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc03: path s-a<=100-b>5000")
        void testDepositPath3() {
            double expectedFeeFromSpec = 0.25;
            // spec needs: 0.5% fee = 0.25. actual code: 0.5% fee = 0.25. this should pass.
            double actualFee = feesCalculator.calculateDepositInterest(50.0, 5001.0, true);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc04: path s-a<=100-b<=5000")
        void testDepositPath4() {
            double expectedFeeFromSpec = 0.00;
            // spec needs: no fee = 0.00. actual code: 0.2% fee = 0.10.
            double actualFee = feesCalculator.calculateDepositInterest(50.0, 5000.0, true);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc05: path ns-a>500-b>5000")
        void testDepositPath5() {
            double expectedFeeFromSpec = 4.008;
            // spec needs: 0.8% fee = 4.008. actual code: 1.0% fee = 5.01.
            double actualFee = feesCalculator.calculateDepositInterest(501.0, 5001.0, false);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc06: path ns-a>500-b<=5000")
        void testDepositPath6() {
            double expectedFeeFromSpec = 2.004;
            // spec needs: 0.4% fee = 2.004. actual code: 0.5% fee = 2.505.
            double actualFee = feesCalculator.calculateDepositInterest(501.0, 2500.0, false);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc07: path ns-a<=500-b>10000")
        void testDepositPath7() {
            double expectedFeeFromSpec = 0.00;
            // spec needs: no fee = 0.00. actual code: 0.5% fee = 1.25.
            double actualFee = feesCalculator.calculateDepositInterest(250.0, 10001.0, false);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }

        @Test
        @DisplayName("dep_tc08: path ns-a<=500-b<=10000")
        void testDepositPath8() {
            double expectedFeeFromSpec = 0.25;
            // spec needs: 0.1% fee = 0.25. actual code: 0.0% fee = 0.00.
            double actualFee = feesCalculator.calculateDepositInterest(250.0, 10000.0, false);
            assertEquals(expectedFeeFromSpec, actualFee, DELTA);
        }
    }

    @Nested
    @DisplayName("Transfer tests (Basis path testing)")
    class TransferTests
    {
        @Test
        @DisplayName("Basis Path 1: s=true, at < 200, fb < 2000, tb <1000, expected fee = 0.1%")
        void testTransferPath1()
        {
            double amountTransferred = 198;
            double expectedFee = amountTransferred * 0.001;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 1998, 998, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 2: s=true, at < 200, fb < 2000, tb >=1000, expected fee = 0.05%")
        void testTransferPath2()
        {
            double amountTransferred = 198;
            double expectedFee = amountTransferred * 0.0005;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 1998, 1002, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 3: s=true, at < 200, fb >= 2000, tb <1000, expected fee = 0.05%")
        void testTransferPath3()
        {
            double amountTransferred = 198;
            double expectedFee = amountTransferred * 0.0005;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 2002, 998, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 4: s=true, at < 200, fb >= 2000, tb >=1000, expected fee = 0.025%")
        void testTransferPath4()
        {
            double amountTransferred = 198;
            double expectedFee = amountTransferred * 0.00025;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 2002, 1002, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 5: s=true, at >= 200, fb < 2000, tb <1000, expected fee = 0.05%")
        void testTransferPath5()
        {
            double amountTransferred = 202;
            double expectedFee = amountTransferred * 0.0005;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 1998, 998, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 6: s=true, at >= 200, fb < 2000, tb >=1000, expected fee = 0.025%")
        void testTransferPath6()
        {
            double amountTransferred = 202;
            double expectedFee = amountTransferred * 0.00025;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 1998, 1002, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 7: s=true, at >= 200, fb >= 2000, tb <1000, expected fee = 0.05%")
        void testTransferPath7()
        {
            double amountTransferred = 202;
            double expectedFee = amountTransferred * 0.0005;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 2002, 998, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 8: s=true, at >= 200, fb >= 2000, tb >=1000, expected fee = 0.125%")
        void testTransferPath8()
        {
            double amountTransferred = 202;
            double expectedFee = amountTransferred * 0.00125;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 2002, 1002, true);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 9: s=false, at < 100, fb < 4000, tb < 2000, expected fee = 0.2%")
        void testTransferPath9()
        {
            double amountTransferred = 98;
            double expectedFee = amountTransferred * 0.002;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 3998, 1998, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 10: s=false, at < 100, fb < 4000, tb >= 2000, expected fee = 0.1%")
        void testTransferPath10()
        {
            double amountTransferred = 98;
            double expectedFee = amountTransferred * 0.001;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 3998, 2002, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 11: s=false, at < 100, fb >= 4000, tb < 2000, expected fee = 1%")
        void testTransferPath11()
        {
            double amountTransferred = 98;
            double expectedFee = amountTransferred * 0.01;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 4002, 1998, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 12: s=false, at < 100, fb >= 4000, tb >= 2000, expected fee = 0.5%")
        void testTransferPath12()
        {
            double amountTransferred = 98;
            double expectedFee = amountTransferred * 0.005;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 4002, 1998, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 13: s=false, at >= 100, fb < 2000, tb < 1000, expected fee = 0.2%")
        void testTransferPath13()
        {
            double amountTransferred = 102;
            double expectedFee = amountTransferred * 0.002;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 1998, 998, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 14: s=false, at >= 100, fb < 2000, tb > 1000, expected fee = 0.1%")
        void testTransferPath14()
        {
            double amountTransferred = 102;
            double expectedFee = amountTransferred * 0.001;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 1998, 1002, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 15: s=false, at >= 100, fb >= 2000, tb < 1000, expected fee = 0.5%")
        void testTransferPath15()
        {
            double amountTransferred = 102;
            double expectedFee = amountTransferred * 0.005;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 2002, 998, false);
            assertEquals(expectedFee, actualFee);
        }

        @Test
        @DisplayName("Basis Path 16: s=false, at >= 100, fb >= 2000, tb >= 1000, expected fee = 0.25%")
        void testTransferPath16()
        {
            double amountTransferred = 102;
            double expectedFee = amountTransferred * 0.0025;
            double actualFee = feesCalculator.calculateTransferFee(amountTransferred, 2002, 1002, false);
            assertEquals(expectedFee, actualFee);
        }
    }
}