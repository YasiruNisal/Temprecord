package com.temprecordapp.yasiruw.temprecord.comms;


/**
 * Created by Yasiru on 20/02/2018.
 */

public class MT2Msg_Read {

    private CommsChar commsChar = new CommsChar();
    /// <summary>Do we wish to allow use of the smallest buffer, normally only required for recovery purposes.</summary>
    private boolean useMicroBuff;
    /// <summary>The device buffer passed in to the class constructor as bufferSize. We can't exceed the size of this buffer no matter how reliable comms are.</summary>
    private int deviceBuffer;
    /// <summary>The memory location associated with this read class. This is the memory we wish to read from the logger.</summary>
    private byte memLoc;
    /// <summary>The number of permited retries before the READ from the device is deemed to be considered a fail.</summary>
    private int retryCount;
    /// <summary>The size of the current buffer we are using. This can dynamically change based on the reliability of communication.</summary>
    private int currentBuffer;
    /// <summary>The current number of retry attempts taken while trying to read the memory from the device.</summary>
    private int retryAttempts;
    /// <summary>A memory variable which we use to keep track of if we should increase/decrease buffer size.</summary>
    private int successCount;
    /// <summary>The current position/address/pointer in the memory read.</summary>
    private int currentAddress;
    /// <summary>Used to add small delays throughout the read mechanism to insure reliability in communicating to the logger.</summary>
    private int sleepCounter;
    /// <summary>The actual byte array of data read from the memory.</summary>
    public byte[] memoryData;

    /// <summary>The total size in bytes of the memory we have been asked to read. Will be set by readSize in the constructor of this class. Normally set to full size of the memory unless we are doing a partial read.</summary>
    public int TotalSize;
    /// <summary>The start address/pointer of the memory we have been asked to read. Will be set by the address parameter in the constructor of this class. Normally set to 0.</summary>
    public int StartAddress;
    /// <summary>The ratio of the memory read so far. Usefull for updating progress bars.</summary>
    public double RatioRead;
    /// <summary>The remaining size of bytes to read from the memory.</summary>
    public int RemainingSize;

    public int reached = 0;
    public int top = 32768;
    int s= 0;


    public MT2Msg_Read(byte location, int address, int readSize, int bufferSize, int allowedRetries)
    {
        deviceBuffer = bufferSize;
        currentBuffer = bufferSize;

        TotalSize = readSize;
        StartAddress = address;
        memLoc = location;

        retryAttempts = 0;
        successCount = 0;

        RemainingSize = Math.abs(readSize - address);
        currentAddress = StartAddress;
        retryCount = allowedRetries;

        //useMicroBuff = recoveryBuffer;

        sleepCounter = 0;

        memoryData = new byte[RemainingSize];

    }


    /// <summary>
    /// Updates the main byte[] array memory from a newly recieved message of bytes from the logger.
    /// This will also update all the pointer variables so we know how far into the read we are
    /// </summary>
    /// <param name="msg">The new byte array read that we should use to update the main byte array of the memory read</param>
    /// <returns>True if successful and everything checks out, null if readSize doesnt match and false if readAddress doesn't match currentAddress</returns>
    private boolean UpdateMessage(byte[] msg)
    {
        int readSize = msg[2];


        //int s =  Math.abs((msg[3] & 0xff) | (msg[4] << 8));
        int s = ((msg[4] & 0xff) << 8) | (msg[3] & 0xff);
        int readAddress = (int) s;

        //Log.d("++++", readAddress+ " == "+ currentAddress + " ///  "+ RemainingSize+ " -- " +  Math.abs(msg[4] << 8) + " " + (msg[3] & 0xff));
        //Check that the readAddress returned matches the currentAddress pointer in this class
        if (readAddress == currentAddress)
        {
            readAddress -= StartAddress;
            System.arraycopy(msg, 5, memoryData, readAddress, readSize);
            currentAddress += readSize;
            RemainingSize -= readSize;
            //Log.d("++++", readSize+ " == "+ currentBuffer + " /  "+ RemainingSize);
            //Check that the readSize matches what we expected from the current buffer
            if (readSize == currentBuffer)
            {   reached = 0;
                return true;
            }
            return Boolean.parseBoolean(null);
        }
        return false;
    }

    public byte[] Read_into_writeByte(boolean start)
    {
        Byte[] msgIn;

        //Decide if this is a SET_READ or a READ.
        //In most cases will be a READ
        byte memCmd = (byte)commsChar.MEM_READ;
        if (start)
        {
            memCmd = (byte)commsChar.MEM_SETR;
        }


        retryAttempts = 0;
            if (currentBuffer > RemainingSize)
            {
                currentBuffer = RemainingSize;
            }

            //memCmd will either be MEM_READ or MEM_SETR (setread) depending on above
            byte[] msgOut = new byte[]
                    {
                            (byte)commsChar.CMD_READ,
                            (byte)memCmd,
                            (byte)memLoc,
                            (byte)currentBuffer,
                            (byte)currentAddress,
                            (byte)(currentAddress >> 8),
                    };

            return msgOut;

    }

    public void BytetoHex(byte[] b){
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b){
            sb.append(String.format("%02X ", b1));

        }
        //Log.i("SPEED", sb.toString());
    }


    public boolean write_into_readByte(byte[] msgIn){
        if(msgIn[0] == commsChar.CMD_ACK){
            boolean updateStatus = UpdateMessage(msgIn);
            //All good!
            //Log.i("USB", "update staus true/false " + updateStatus);
            if (updateStatus == true) {

                if (RemainingSize == 0) {
                    return true;
                }
                else {
                    return false;
                }
            }else {
                return false;
            }
        }else
        return false;
    }


}
