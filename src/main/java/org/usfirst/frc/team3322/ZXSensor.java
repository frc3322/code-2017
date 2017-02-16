package org.usfirst.frc.team3322;

// Created by snekiam on 1/19/2017.
 
public class ZXSensor {
    //register addresses
    public static final int ZX_MODEL_VER = 0x00;
    public static final int ZX_DRE = 0x01;
    public static final int ZX_DRCFG = 0x02;
    public static final int ZX_GESTURE = 0x04;
    public static final int ZX_GSPEED = 0x05;
    public static final int ZX_DCM = 0x06;
    public static final int ZX_XPOS = 0x08;
    public static final int ZX_ZPOS = 0x0A;
    public static final int ZX_LRNG = 0x0C;
    public static final int ZX_RRNG = 0x0E;
    public static final int ZX_REGVER = 0xFE;
    public static final int ZX_MODEL = 0xFF;
    //names of sensors
    public static final int DRE_RNG = 0;
    public static final int DRE_CRD = 1;
    public static final int DRE_SWP = 2;
    public static final int DRE_HOVER = 3;
    public static final int DRE_HVG = 4;
    public static final int DRE_EDGE = 5;
    public static final int DRCFG_POLARITY = 0;
    public static final int DRCFG_EDGE = 1;
    public static final int DRCFG_FORCE = 6;
    public static final int DRCFG_EN = 7;

    //UART Message Headers
    public static final int ZX_UART_END = 0xFF;
    public static final int ZX_UART_RANGES = 0xFE;
    public static final int ZX_UART_X = 0xFA;
    public static final int ZX_UART_Z = 0xFB;
    public static final int ZX_UART_GESTURE = 0xFC;
    public static final int ZX_UART_ID = 0xF1;
}
