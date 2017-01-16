package com.nishtahir.androidthings.liquidcrystal.io;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.things.pio.Gpio;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Liquid crystal driver.
 */
public class LiquidCrystal implements Runnable {

    private static final String TAG = LiquidCrystal.class.getSimpleName();

    private static final byte LCD_4_BIT_OPERATING_MODE = 0x02;
    private static final byte LCD_8_BIT_FUNCTION = 0x28;

    /**
     * Display Data Ram Address.
     * Has to be int, no unsigned byte unfortunately...
     */
    private static final int LCD_SET_DDRAM_ADDR = 0x80;

    private static final byte LCD_DISPLAY_ON = 0x0F;
    private static final byte LCD_CLEAR_DISPLAY = 0x01;

    @NonNull
    private Gpio resetPin;

    @NonNull
    private Gpio enablePin;

    @NonNull
    private List<Gpio> dataBus;

    /**
     * @param rs
     * @param e
     * @param d4
     * @param d5
     * @param d6
     * @param d7
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

        init();
    }

    /**
     * @throws IOException
     */
    private void init() throws IOException {
        sendCommand(LCD_4_BIT_OPERATING_MODE, /*four bit mode*/ true);
        sendCommand(LCD_8_BIT_FUNCTION);
        sendCommand(LCD_DISPLAY_ON);
        sendCommand((byte) 0x06);
        sendCommand(LCD_CLEAR_DISPLAY);
    }

    public void write(@NonNull final String value) throws IOException {
        for (char c : value.toCharArray()) {
            write(c);
        }
    }

    private void write(char c) throws IOException {
        resetPin.setValue(true);
        write8((byte) c);
    }

    public void write(byte... val) throws IOException {
        resetPin.setValue(true);
        for (byte b : val) {
            write8(b);
        }
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
        delay(1);
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
        delay(1);
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
    }

    private void pulseEnable() throws IOException {
        enablePin.setValue(false);
        delay(1);
        enablePin.setValue(true);
        delay(1);
        enablePin.setValue(false);
        delay(1);
    }

    /**
     * Terminates connection to the LCD display.
     *
     * @throws IOException
     */
    public void shutDown() throws IOException {
        resetPin.close();
        enablePin.close();

        for (Gpio pin : dataBus) {
            pin.close();
        }
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

}
