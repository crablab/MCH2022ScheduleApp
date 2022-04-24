package nerd.tuxmobil.fahrplan.congress.schedule

import com.google.common.truth.Truth.assertThat
import nerd.tuxmobil.fahrplan.congress.NoLogging
import org.junit.Test

class HorizontalSnapScrollStateTest {

    @Test
    fun `assert default values`() {
        with(createState()) {
            assertThat(xStart).isEqualTo(0)
            assertThat(displayColumnCount).isEqualTo(1)
            assertThat(roomsCount).isEqualTo(Int.MIN_VALUE)
            assertThat(columnWidth).isEqualTo(0)
            assertThat(activeColumnIndex).isEqualTo(0)
            assertThat(isRoomsCountInitialized()).isEqualTo(false)
        }
    }

    @Test
    fun `activeColumnIndex is reset to its minimum value`() {
        assertThat(createState().copy(activeColumnIndex = -1).activeColumnIndex).isEqualTo(0)
    }

    @Test
    fun `xStart is constraint to positive values`() {
        try {
            HorizontalSnapScrollState(NoLogging, xStart = -1)
        } catch (e: IllegalStateException) {
            assertThat(e.message).isEqualTo("xStart cannot be less then 0 but is -1.")
        }
    }

    @Test
    fun `displayColumnCount is constraint to values greater than 0`() {
        try {
            HorizontalSnapScrollState(NoLogging, displayColumnCount = 0)
        } catch (e: IllegalStateException) {
            assertThat(e.message).isEqualTo("displayColumnCount cannot be 0.")
        }
    }

    @Test
    fun `columnWidth is constraint to values greater than 0`() {
        try {
            HorizontalSnapScrollState(NoLogging, columnWidth = -1)
        } catch (e: IllegalStateException) {
            assertThat(e.message).isEqualTo("columnWidth cannot be less then 0 but is -1.")
        }
    }

    @Test
    fun `isRoomsCountInitialized returns true if roomCount is greater than 0`() {
        assertThat(createState().copy(roomsCount = 23).isRoomsCountInitialized()).isTrue()
    }

    @Test
    fun `calculateScrollToXPosition returns columnIndex x columnWidth`() {
        with(createState().copy(columnWidth = 993)) {
            assertThat(calculateScrollToXPosition(measuredWidth = 53622, columnIndex = 1)).isEqualTo(993)
        }
    }

    @Test
    fun `calculateScrollToXPosition returns 0 if columnWidth = 0`() {
        with(createState().copy(columnWidth = 0)) {
            assertThat(calculateScrollToXPosition(measuredWidth = 53622, columnIndex = 0)).isEqualTo(0)
        }
    }

    @Test
    fun `calculateScrollToXPosition returns the minColumnIndex if columnIndex = -1`() {
        with(createState().copy(columnWidth = 993)) {
            assertThat(calculateScrollToXPosition(measuredWidth = 53622, columnIndex = -1)).isEqualTo(0)
        }
    }

    @Test
    fun `calculateScrollToXPosition returns the maxColumnIndex x columnWidth if columnIndex exceeds maxColumnIndex`() {
        with(createState().copy(columnWidth = 993)) {
            assertThat(calculateScrollToXPosition(measuredWidth = 53622, columnIndex = 54)).isEqualTo(52629)
        }
    }

    @Test
    fun `calculateColumnWidth returns 0 if measuredWidth = 0`() {
        with(createState()) {
            assertThat(calculateColumnWidth(measuredWidth = 0)).isEqualTo(0)
        }
    }

    @Test
    fun `calculateColumnWidth returns 1 if measuredWidth = 1`() {
        with(createState().copy(displayColumnCount = 1)) {
            assertThat(calculateColumnWidth(measuredWidth = 1)).isEqualTo(1)
        }
    }

    @Test
    fun `calculateColumnWidth returns 993 if for a Pixel 6 in portrait mode`() {
        with(createState().copy(displayColumnCount = 1)) {
            assertThat(calculateColumnWidth(measuredWidth = 993)).isEqualTo(993)
        }
    }

    @Test
    fun `calculateColumnWidth returns 555 for a Pixel 6 in landscape mode`() {
        with(createState().copy(displayColumnCount = 4)) {
            assertThat(calculateColumnWidth(measuredWidth = 2219)).isEqualTo(555)
        }
    }

    private fun createState() = HorizontalSnapScrollState(
        logging = NoLogging
    )

}
