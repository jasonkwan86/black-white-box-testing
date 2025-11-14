package bank.utils;

import org.junit.jupiter.api.*;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeesCalculatorTest {
    private FeesCalculator feesCalculator;
    private static final double WITHDRAWAL_AMOUNT = 100.0;
    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() throws Exception {
        feesCalculator = new FeesCalculator();
    }

    @AfterEach
    void tearDown() throws Exception {
    }


    @Nested
    @DisplayName("student withdrawal fee tests")
    class StudentWithdrawalTests {

        @Test
        @DisplayName("test 1: student on a weekday should have NO fee")
        void student_weekday_no_fee() {
            // should fail, code charges but spec said it shouldnt
            double expectedFee = 0.00;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, true, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 2: Student on a weekend should have 0.1% fee")
        void student_weekend_has_fee() {
            // should fail, code doesnt charge but spec said it should
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001; // 0.1%
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 500.0, true, Calendar.SATURDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }
    }

    @Nested
    @DisplayName("non student withdrawal fee tests")
    class NonStudentWithdrawalTests {

        @Test
        @DisplayName("test 1: balance -500.00 should have 0.3% fee")
        void balance_negative_is_point_three_percent_fee() {
            // should fail, spec says 0.3%, code says 0.2%
            double expectedFee = WITHDRAWAL_AMOUNT * 0.003;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, -500.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 2: balance 0.00 should have 0.3% fee")
        void balance_zero_is_point_three_percent_fee() {
            // should fail, spec says 0.3%, code says 0.2%
            double expectedFee = WITHDRAWAL_AMOUNT * 0.003;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 0.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 3: balance 999.99 should have 0.3% fee")
        void balance_just_below_1000_is_point_three_percent_fee() {
            // should fail, spec says 0.3%, code says 0.2%
            double expectedFee = WITHDRAWAL_AMOUNT * 0.003;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 999.99, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 4: balance 1000.00 should have 0.1% fee")
        void balance_at_1000_is_point_one_percent_fee() {
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 1000.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 5: balance 4999.99 should have 0.1% fee")
        void balance_just_below_5000_is_point_one_percent_fee() {
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 4999.99, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 6: balance 5000.00 should have 0.1% fee per spec")
        void balance_at_5000_is_point_one_percent_fee() {
            double expectedFee = WITHDRAWAL_AMOUNT * 0.001;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 5000.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 7: balance 5000.01 should have NO fee per spec")
        void balance_just_above_5000_is_no_fee() {
            // should fail, spec says no fee over 5000, but code charges 0.1%
            double expectedFee = 0.00;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 5000.01, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }

        @Test
        @DisplayName("test 8: balance 7500.00 (nominal) should have NO fee per spec")
        void balance_nominal_above_5000_is_no_fee() {
            // should fail, spec says no fee, but code charges 0.1% as it's below 10000
            double expectedFee = 0.00;
            double actualFee = feesCalculator.calculateWithdrawalFee(WITHDRAWAL_AMOUNT, 7500.00, false, Calendar.MONDAY);
            assertEquals(expectedFee, actualFee, DELTA);
        }
    }
}