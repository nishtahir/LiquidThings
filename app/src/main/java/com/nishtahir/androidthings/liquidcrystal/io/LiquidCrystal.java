package com.nishtahir.androidthings.liquidcrystal.io;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.things.pio.Gpio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Liquid crystal driver.
 */
public class LiquidCrystal implements Runnable, AutoCloseable {

    private static final String TAG = LiquidCrystal.class.getSimpleName();

    private static final byte LCD_4_BIT_OPERATING_MODE = 0x02;
    private static final byte LCD_8_BIT_FUNCTION = 0x28;

    /**
     * Display Data Ram Address.
     * Has to be int, no unsigned byte unfortunately...
     */
    private static final int LCD_SET_DDRAM_ADDR = 0x80;
    private static final byte LCD_DDRAM_ADDR_COL1_ROW0 = 0x40;

    private static final byte LCD_DISPLAY_ON = 0x0F;
    private static final byte LCD_CLEAR_DISPLAY = 0x01;
    private static final byte LCD_SET_ENTRY_MODE_NO_SHIFT_DISPLAY = 0x06;

    private static final byte ROWS = 2;
    private static final byte COLUMNS = 16;

    @NonNull
    private Gpio resetPin;

    @NonNull
    private Gpio enablePin;

    @NonNull
    private List<Gpio> dataBus;

    /**
     *
     * d4..d7 Four high order bidirectional tristate data bus pins.
     * Used for data transfer and receive between the MPU and the HD44780U.
     * DB7 can be used as a busy flag.
     *
     * @param rs Selects registers.
     *           0: Instruction register (for write) Busy flag:address counter (for read)
     *           1: Data register (for write and read)
     * @param e  Starts data read/write
     * @throws IOException
     */
    public LiquidCrystal(@NonNull Gpio rs,
                         @NonNull Gpio e,
                         @NonNull Gpio d4,
                         @NonNull Gpio d5,
                         @NonNull Gpio d6,
                         @NonNull Gpio d7) throws IOException, InterruptedException {
        this.resetPin = rs;
        this.enablePin = e;

        rs.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        e.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        dataBus = Arrays.asList(d4, d5, d6, d7);
        for (Gpio bit : dataBus) {
            bit.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        }

        sendCommand(LCD_4_BIT_OPERATING_MODE, /*four bit mode*/ true);
        sendCommand(LCD_8_BIT_FUNCTION);
        sendCommand(LCD_DISPLAY_ON);
        sendCommand(LCD_SET_ENTRY_MODE_NO_SHIFT_DISPLAY);
        sendCommand(LCD_CLEAR_DISPLAY);
    }

    public void write(@NonNull String val) throws IOException {
        resetPin.setValue(true);
        for (char b : val.toCharArray()) {
            write(b);
        }
    }

    private void write(char c) throws IOException {
        resetPin.setValue(true);
        write8((byte) c);
    }

    public void setCursor(@IntRange(from = 1, to = ROWS) int row,
                          @IntRange(from = 1, to = COLUMNS) int column) throws IOException {
        sendCommand((byte) (LCD_SET_DDRAM_ADDR | ((LCD_DDRAM_ADDR_COL1_ROW0 * (row - 1)) + (column - 1))));
    }

    /**
     * @param command
     * @param fourBitMode
     * @throws IOException
     */
    private void sendCommand(byte command, boolean fourBitMode) throws IOException {
        resetPin.setValue(false);
        if (fourBitMode) {
            write4(command);
        } else {
            write8(command);
        }
    }

    /**
     * Sends a command to the display controller.
     *
     * @param command
     * @throws IOException
     */
    private void sendCommand(byte command) throws IOException {
        sendCommand(command, false);
    }

    /**
     * Write 8 bits to the data bus using GPIO.
     *
     * @param value Value to write. Assumes <b>unsigned.</b>
     * @throws IOException
     */
    private void write8(byte value) throws IOException {
        write4((byte) (value >> 4));
        write4(value);
    }

    /**
     * Write 4 bits to the data bus using GPIO.
     *
     * @param value Value to write. Assumes <b>unsigned.</b>
     * @throws IOException
     */
    private void write4(byte value) throws IOException {
        for (int i = 0; i < dataBus.size(); i++) {
            Gpio pin = dataBus.get(i);
            pin.setValue(((value >> i & 0x01) != 0));
        }
        pulseEnable();
        delay(1);
    }

    private void pulseEnable() throws IOException {
        enablePin.setValue(false);
        delay(1);
        enablePin.setValue(true);
        delay(1);
        enablePin.setValue(false);
        delay(1);
    }

    @Override
    public void run() {

    }

    /**
     * Convenience method, just in case my threading strategy changes.
     *
     * @param ms time in ms to wait.
     */
    private void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        resetPin.close();
        enablePin.close();

        for (Gpio pin : dataBus) {
            pin.close();
        }
    }
}
