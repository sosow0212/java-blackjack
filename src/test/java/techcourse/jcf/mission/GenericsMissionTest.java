package techcourse.jcf.mission;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GenericsMissionTest {

    @Test
    @DisplayName("mission 2")
    void mission_one() {
        // given
        final String[] arrays = {"first", "second"};

        // when
        final SimpleList<String> values = SimpleList.<String>fromArrayToList(arrays);

        // then
        assertThat(values.contains(arrays[0])).isTrue();
        assertThat(values.contains(arrays[1])).isTrue();
    }

    @Test
    @DisplayName("mission 3")
    void mission_three() {
        // given
        final SimpleList<Double> doubleValues = new SimpleArrayList<Double>(0.5, 0.7);
        final SimpleList<Integer> intValues = new SimpleArrayList<Integer>(1, 2);

        // when
        final double doubleTotal = SimpleList.sum(doubleValues); // 1.2
        final double intTotal = SimpleList.sum(intValues);  // 3

        // then
        assertThat(doubleTotal).isEqualTo(1.2);
        assertThat(intTotal).isEqualTo(3);
    }

    @Test
    @DisplayName("mission 4")
    void mission_four() {
        // given
        final SimpleList<Double> doubleValues = new SimpleArrayList<Double>(-0.1, 0.5, 0.7);
        final SimpleList<Integer> intValues = new SimpleArrayList<Integer>(-10, 1, 2);

        // when
        final SimpleList<Double> filteredDoubleValues = SimpleList.filterNegative(doubleValues);
        final SimpleList<Integer> filteredIntValues = SimpleList.filterNegative(intValues);

        // then
        assertThat(filteredDoubleValues.size()).isEqualTo(2);
        assertThat(filteredDoubleValues.contains(-0.1)).isFalse();

        assertThat(filteredIntValues.size()).isEqualTo(2);
        assertThat(filteredIntValues.contains(-10)).isFalse();
    }

    @Test
    @DisplayName("mission 5")
    void mission_five() {
        // given
        final var laserPrinter = new LaserPrinter(10);

        final SimpleList<Printer> printers = new SimpleArrayList<Printer>();
        printers.add(new Printer(0));
        printers.add(new Printer(1));

        final SimpleList<LaserPrinter> laserPrinters = new SimpleArrayList<LaserPrinter>(laserPrinter);

        // when
        SimpleList.copy(laserPrinters, printers);

        // then
        assertThat(printers.get(0) == laserPrinters.get(0)).isTrue();
    }
}
