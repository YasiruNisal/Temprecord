package youten.redo.yasiru.util;

/**
 * Created by Yasiru on 08-Dec-17.
 */

public class CommsChar {

    public final byte CHAR_RET = 0x0D;
    public final byte CHAR_ESC = 0x1B;
    public final byte CHAR_CLR = 0x55;
    public static final byte CESC_ESC = (byte)0x00;
    public static final byte CESC_RET = (byte)0x01;
    public static final byte CESC_CLR = (byte)0x02;

    // The main commands that the logger accepts.
    public final byte CMD_NAK = (byte)0xFF;
    public final byte CMD_ACK = 0x00;
    public final byte CMD_START = 0x04;
    public final byte CMD_STOP = 0x06;
    public final byte CMD_REUSE = 0x07;
    public final byte CMD_TAG = 0x05;

    // The following commands are used to diagnose why the logger sent a NACK
    public final byte NAK_NONE = (byte)0xFF;
    public final byte NAK_NOP = (byte)0xFE;
    public final byte NAK_RXERR = (byte)0xFD;
    public final byte NAK_CRC = (byte)0xFC;
    public final byte NAK_LEN = (byte)0xFB;
    public final byte NAK_MEM = (byte)0xFA;
    public final byte NAK_LOGIN = (byte)0xF9;
    public final byte NAK_USER = (byte)0xF0;


    public final byte MODE_SLEEP = (byte)0;
    public final byte MODE_NOCFG = (byte)1;
    public final byte MODE_READY = (byte)2;
    public final byte MODE_DELAY = (byte)3;
    public final byte MODE_RUN = (byte)4;
    public final byte MODE_STOP = (byte)5;
    public final byte MODE_REUSE = (byte)6;
    public final byte MODE_ERROR = (byte)7;
    public final byte MODE_UNKNOWN = (byte)0xFF;





}
