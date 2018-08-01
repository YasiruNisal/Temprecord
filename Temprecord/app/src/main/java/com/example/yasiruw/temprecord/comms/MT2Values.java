package com.example.yasiruw.temprecord.comms;

import android.util.Log;


import com.example.yasiruw.temprecord.Types.baseType;
import com.example.yasiruw.temprecord.utils.CommsChar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Yasiru on 7/03/2018.
 */

public class MT2Values  {

    private CommsChar commsChar = new CommsChar();
    private static QueryStrings queryStrings = new QueryStrings();

    public static class MT2Mem_values extends baseType.baseClass {

        /// <summary>The maximum byte size taken up by the logged samples on the logger.</summary>
        public static final int ByteSize = 32768;

        /// <summary>The Mon-T2 Parent from which we need to extract some information to correctly displayed the logged data</summary>
        private classMessages msg = new classMessages();

        /// <summary>The raw logged data as read from a file/data logger</summary>
        private ArrayList<Byte> RawData;

        public boolean HasBeenRead;

        /// <summary>The data values of the read data for all channels</summary>
        public ArrayList<sVal_Data> Data;

        /// <summary>The tag/marker data with timestamp for ease of displaying</summary>
        public ArrayList<sVal_TagData> TagData;

        /// <summary>The total number of tags/markers present in the read data</summary>
        public int TagCount()
        {
            return TagData.size();
        }

        /// <summary>The statistics calculated from data for Channel 0 (Temperature)</summary>
        public sVal_MKTStats ch0Stats;

        /// <summary>The statistics calculated from data for Channel 1 (RH)</summary>
        public sVal_Stats ch1Stats;

        /// <summary>The total time span of the logged data</summary>
        public long LoggedTripDuration;

        /// <summary>Total number of logged samples present in the read data</summary>
        public int LoggedSampleCount()
        {
            if(Data != null)
            {
                return Data.size();
            }
            else
            {
                return 0;
            }
        }
        /// <summary>
        /// Memory Values constructor from a Mon-T2 class with no values read
        /// </summary>
        /// <param name="parent">The Mon-T2 parent object to inherit properties from</param>
        public MT2Mem_values(){
            super(ByteSize);


            HasBeenRead = false;
            RawData = new ArrayList<>();
            Data = new ArrayList<>();
            TagData = new ArrayList<>();
            ch0Stats = new sVal_MKTStats(60,-30);
            ch1Stats = new sVal_Stats(90, 20);
        }

        public MT2Mem_values(ArrayList<Byte> data, Date firstLogSample, int Ch0Hi, int Ch0Lo, int Ch1Hi, int Ch1Lo, int samplePeriod,
        boolean ch0Enable, boolean ch1Enable) throws ParseException {
            super(ByteSize);

            ch0Stats = new sVal_MKTStats(Ch0Hi, Ch0Lo);
            ch1Stats = new sVal_Stats(Ch1Hi, Ch1Lo);

            if(data == null || data.size() == 0){
                HasBeenRead = false;
                RawData = new ArrayList<>();
                Data = new ArrayList<>();
                TagData = new ArrayList<>();
            }else{
                HasBeenRead = true;
                RawData = data;
                //Log.i("READ" , " Raw data size" + RawData.size());
                FillData(RawData, firstLogSample, samplePeriod, ch0Enable, ch1Enable);
            }
        }

        private void FillData(ArrayList<Byte> rawData, Date firstLoggedSample, int samplePeriod, boolean ch0Enable, boolean ch1Enable) throws ParseException {
            Data = new ArrayList<>();
            TagData = new ArrayList<>();
            boolean hasFlag = false;
            short sample1 = 0;
            short sample2 = 0;
            int sample = 1;
            sVal_Data tempData;

            long SamplePeriod = samplePeriod;
            Date sampleDateTime = firstLoggedSample;
            //when both channels are enabled
            if(ch1Enable && ch0Enable){
                //Log.i("READ" , " Coming into Channel 1 and channel 2 "+ rawData.size());
                for(int i = 0; i < rawData.size(); i++) {

                    sample1 = (short)(rawData.get(i) & 0xff);
                    i++;
                    sample1 |= (short) (rawData.get(i) << 8);
                    i++;
                    sample2 = (short)(rawData.get(i) & 0xff);
                    i++;
                    sample2 |= (short) (rawData.get(i) << 8);
                    hasFlag = ((sample1 & (0x0002)) != 0);


                    tempData = new sVal_Data(sample, sampleDateTime, hasFlag, (sample1 >> 2), (sample2 >> 2));
                    ch0Stats.Update(tempData.valCh0, sample, sampleDateTime);
                    ch1Stats.Update(tempData.valCh1, sample, sampleDateTime);
                    Data.add(tempData);
                    if (hasFlag) {
                        TagData.add(new sVal_TagData(sampleDateTime));
                    }
                    //Log.i("READ" , " Sample size " + sample);
                    sample++;
                    Calendar calendar = queryStrings.toCalendar(sampleDateTime);
                    calendar.add(Calendar.SECOND, samplePeriod);
                    sampleDateTime = calendar.getTime();
                }

                ch0Stats.Finalize(rawData.size() / 4, samplePeriod);
                ch1Stats.Finalize(rawData.size() / 4, samplePeriod);
                LoggedTripDuration = (SamplePeriod * (rawData.size() / 4));
                //when channel 1 enabled
            }else if(ch0Enable){
                for (int i = 0; i < rawData.size(); i++) {
                    sample1 = (short)(rawData.get(i) & 0xff);
                    i++;
                    sample1 |= (short) (rawData.get(i) << 8);
                    hasFlag = ((sample1 & (0x0002)) != 0);


                    tempData = new sVal_Data(sample, sampleDateTime, hasFlag, (sample1 >> 2), 0);
                    ch0Stats.Update(tempData.valCh0, sample, sampleDateTime);
                    Data.add(tempData);
                    if (hasFlag)
                    {
                        TagData.add(new sVal_TagData(sampleDateTime));
                    }

                    sample++;
                    Calendar calendar = queryStrings.toCalendar(sampleDateTime);
                    calendar.add(Calendar.SECOND, samplePeriod);
                    sampleDateTime = calendar.getTime();

                }

                ch0Stats.Finalize(rawData.size() / 2, samplePeriod);
                LoggedTripDuration = (SamplePeriod * (rawData.size() / 2));

            }else {//channel 2 enabled
                for (int i = 0; i < rawData.size(); i++)
                {
                    sample2 = (short)(rawData.get(i) & 0xff);
                    i++;
                    sample2 |= (short)(rawData.get(i) << 8);
                    hasFlag = ((sample2 & (0x0002)) != 0);

                    tempData = new sVal_Data(sample, sampleDateTime, hasFlag, 0, (sample2 >> 2));
                    ch1Stats.Update(tempData.valCh1, sample, sampleDateTime);
                    Data.add(tempData);
                    if (hasFlag)
                    {
                        TagData.add(new sVal_TagData(sampleDateTime));
                    }

                    sample++;
                    Calendar calendar = queryStrings.toCalendar(sampleDateTime);
                    calendar.add(Calendar.SECOND, samplePeriod);
                    sampleDateTime = calendar.getTime();
                }

                ch1Stats.Finalize(rawData.size() / 2, samplePeriod);
                LoggedTripDuration = samplePeriod * (rawData.size() / 2);

            }
        }


        // <summary>
        /// The method that converts this class into a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to append this classes bytes to.</param>
        @Override
        public void ToByte(ArrayList<Byte> data)
        {
            for(int i = 0 ; i < RawData.size(); i++) {
                data.add(RawData.get(i));
            }
        }


        /// <summary>
        /// A size check for the class, this time calculating the size based
        /// on the sum of all the types in the class rather than a value passed.
        /// </summary>
        /// <returns>The total size of bytes on the device that this class takes up</returns>
        @Override
        public int CalculateSize()
        {
            return RawData.size();
        }

    }

    public static class sVal_TagData{
        /// <summary>
        /// The tag data constructor. Just requires a timestamp for when the tag was placed.
        /// </summary>
        /// <param name="time">The timestamp indicating the time the tag was placed/added</param>
        public sVal_TagData(Date time)
        {
            valTime = time;
        }

        /// <summary>An internal UTC timestamp used in the constructor to indicate the timestamp when the tag was placed/added</summary>
        protected Date valTime;

        /// <summary>The UTC timestamp for the tag</summary>
        public Date ValTimeUTC()
        {
             return valTime;
        }

        /// <summary>The local PC timestamp for the tag</summary>
        public Date ValTimeLocal()
        {
            return valTime;
        }

        /// <summary>Returns 0 to display a value on the Graph at the Y=0 location</summary>
        public int ValTag(){
            return 0;
        }
    }


    public static class sVal_Data{

        /// <summary>
        /// Data point value constructor used to store all the data points at a single point in time
        /// </summary>
        /// <param name="number">The sequential number of this data logged point</param>
        /// <param name="time">The timestamp indicating at which point in time these points were taken at</param>
        /// <param name="tag">Indicates if we have a tag/marker present</param>
        /// <param name="ch0">The value for Channel 0 at this point in time</param>
        /// <param name="ch1">The value for Channel 1 (RH) at this point in time</param>
        public sVal_Data(int number, Date time, boolean tag, int ch0, int ch1)
        {
            ValSample = number;
            valTime = time;
            ValTag = tag;
            valCh0 = ch0;
            //valueCh0F = AppConstants.UnitConv(valueCh0);

            valCh1 = ch1;
        }

        /// <summary>The sequential number/pointer of this sample</summary>
        public int ValSample;

        /// <summary>An internal UTC timestamp used in the constructor to indicate the timestamp when the data was created</summary>
        public Date valTime;

        /// <summary>The UTC timestamp for the data</summary>
//        public Date ValTimeUTC()
//        {
//            return valTime.getTime();
//        }
//
//        /// <summary>The local PC timestamp for the data</summary>
//        public Date ValTimeLocal()
//        {
//            return valTime.getTime();
//        }

        /// <summary>Is there a marker/tag present?</summary>
        public boolean ValTag;

        /// <summary>A null/int option for if there is a tag present? Returns 0 if present, null if not.</summary>
        public int intTag()
        {
            return ValTag ? 0 : 1;
        }

        /// <summary>The integer (sVal16_1) value of channel 0 at this point in time</summary>
        public int valCh0;

        /// <summary>The double value of channel 0 in Celsius at this point in time</summary>
        public double valueCh0()
        {
             return valCh0 / 10.0;
        }

        /// <summary>The double value of channel 0 in Fahrenheit at this point in time</summary>
        public double valueCh0F(){
                return ((9/5) * valueCh0() + 32);

        }

        /// <summary>The integer (sVal16_1) value of channel 1 at this point in time</summary>
        public int valCh1;

        /// <summary>The double value of channel 1 in %RH at this point in time</summary>
        public double valueCh1()
        {
             return valCh1 / 10.0;
        }



    }


    public static class sVal_Stats{

        /// <summary>
        /// Set up a new statistics object for a channel which will then calculate all the necessary statistics that we wish to use or display to the end user.
        /// </summary>
        /// <param name="upperLimit">The upper limit of this channel to be used to calculate time/percentage above the upper limit</param>
        /// <param name="lowerLimit">The lower limit of this channel to be used to calculate time/percentage below the lower limit</param>
        public sVal_Stats(int upperLimit, int lowerLimit){
            Max = new sVal_StatVal(-9999);
            Min = new sVal_StatVal(9999);
            Mean = 0;

            UpperLimit = upperLimit;
            LowerLimit = lowerLimit;
            AboveLimit = 0;
            BelowLimit = 0;
        }

        /// <summary>
        /// Update the channel statistics with a new value read from the data array. To be called every time we read in a new sample.
        /// </summary>
        /// <param name="value">The new data logged value to update the statistics with</param>
        /// <param name="number">The sequence number of the new data logged value</param>
        /// <param name="time">The timestamp of the new data logged value</param>
        public void Update(int value, int number, Date time)
        {

            Mean += value;
            if (Min.MinCompare(value))
            {
                Min = new sVal_StatVal(value, number, time);

            }
            if (Max.MaxCompare(value))
            {
                Max = new sVal_StatVal(value, number, time);
            }

            if (value/10.0 > UpperLimit)
            {
                AboveLimit++;
            }
            else if (value/10.0 < LowerLimit)
            {
                BelowLimit++;
            }
        }

        /// <summary>
        /// Finalizes the statistics calculations. To be called at the very end when we finished processing all the samples.
        /// For instance, average can only be calculated from the total sum/total samples so this gets calculated by calling this function.
        /// </summary>
        /// <param name="totalCount">The total number of samples we have read in and wish to calculate statistics for</param>
        /// <param name="samplePeriod">The sample period in seconds to use when calculating time above/below limits</param>
        public void Finalize(int totalCount, int samplePeriod)
        {
            Mean = Mean / (double)totalCount;
            Mean = Mean / 10.0;

            AbovePercent = AboveLimit / (double)totalCount * 100.0;
            BelowPercent = BelowLimit / (double)totalCount * 100.0;

            AboveTime = queryStrings.Period(samplePeriod * AboveLimit*1000);
            BelowTime = queryStrings.Period(samplePeriod * BelowLimit*1000);

            TotalLimit = AboveLimit + BelowLimit;
            TotalPercent = AbovePercent + BelowPercent;
            TotalTime = queryStrings.Period(samplePeriod * TotalLimit);

            TotalLimitWithin = totalCount - TotalLimit;
            TotalPercentWithin = 100.0 - TotalPercent;
            TotalTimeWithin = queryStrings.Period(samplePeriod * TotalLimitWithin*1000);

            //Log.d("____++++++++++++_______", TotalTimeWithin +" "+ samplePeriod+ " "+TotalLimitWithin );
        }


        public sVal_StatVal Min;

        /// <summary>The maximum value for this channel as well as the timestamp and sequence number</summary>
        public sVal_StatVal Max;

        /// <summary>The mean/average for this channel</summary>
        public double Mean;

        /// <summary>The upper limit for this channel as specified in the constructor</summary>
        private int UpperLimit;

        /// <summary>The lower limit for this channel as specified in the constructor</summary>
        private int LowerLimit;

        /// <summary>Total samples within limits</summary>
        public int TotalLimitWithin;

        /// <summary>Total percentage of samples within limits</summary>
        public double TotalPercentWithin;

        /// <summary>Total time spent within limits</summary>
        public String TotalTimeWithin;

        /// <summary>Total samples outside of limits</summary>
        public int TotalLimit;

        /// <summary>Total percentage of samples outside of limits</summary>
        public double TotalPercent;
        /// <summary>Total time outside of limits</summary>
        public String TotalTime;

        /// <summary>Upper Limit Exceeded in the read data (ignores limit delay)</summary>
        public boolean UpperLimitFlag()
        {
            return (AboveLimit > 0);
        }

        /// <summary>Lower Limit Exceeded in the read data (ignores limit delay)</summary>
        public boolean LowerLimitFlag()
        {
            return (BelowLimit > 0);
        }

        /// <summary>Total samples above upper limit</summary>
        public int AboveLimit;

        /// <summary>Total samples below lower limit</summary>
        public int BelowLimit;

        /// <summary>Total percentage of samples above upper limit</summary>
        public double AbovePercent;

        /// <summary>Total percentage of samples below lower limit</summary>
        public double BelowPercent;

        /// <summary>Total time spent above upper limit</summary>
        public String AboveTime;

        /// <summary>Total time spent below lower limit</summary>
        public String BelowTime;
    }





    public static class sVal_StatVal {

        public int Value;
        public int Number;
        public Date Occurance;

        public sVal_StatVal(int init){
            Value = init;
            Number = -1;
            Occurance  = Calendar.getInstance().getTime();
        }

        public sVal_StatVal(int value, int number, Date time){
            Value = value;
            Number = number;
            Occurance = time;
        }


        /// <summary>
        /// Compares the value and returns true if it is smaller than the current value stored in this class.
        /// </summary>
        /// <param name="compareVal">The value to compare the current minimum value to</param>
        /// <returns>True if the compareVal is less than the current value. False otherwise.</returns>
        public boolean MinCompare(int compareVal)
        {
            //Log.d("____++++++++++++_______", this.Value +" "+ compareVal+ " " );
            if (this.Value != compareVal)
            {
                if (Math.min(this.Value, compareVal) == compareVal)
                {
                    return true;
                }
            }
            return false;
        }

        /// <summary>
        /// Compares the value and returns true if it is larger than the current value stored in this class.
        /// </summary>
        /// <param name="compareVal">The value to compare the current maximum value to</param>
        /// <returns>True if the compareVal is more than the current value. False otherwise.</returns>
        public boolean MaxCompare(int compareVal)
        {
            if (this.Value != compareVal)
            {
                if (Math.max(this.Value, compareVal) == compareVal)
                {
                    return true;
                }
            }
            return false;
        }


        public double getDouble(){
            return Value/10.0;
        }


    }


    public static class sVal_MKTStats extends sVal_Stats {
        /// <remarks>
        /// The following constants are used for MKT calculations both in software and on the data logger.
        /// The data logger simplifies the calculation and assumes that DELTAH/R = 10
        /// Be careful if you change these constants as then the software and firmware could have larger discrepencies
        /// </remarks>
        /// <summary>The DELTAH is a constant used for MKT calculations</summary>
        private static final double DELTAH = 83.14472;
        /// <summary>The R is the Gas Constant used for MKT calculations</summary>
        private static final double R = 8.314472;

        /// <summary>
        /// Set up a new statistics object for a temperature channel which will then calculate all the necessary statistics that we wish to use or display to the end user.
        /// Works for temperature only as RH does not support MKT!
        /// </summary>
        /// <param name="upperLimit">The upper limit of this channel to be used to calculate time/percentage above the upper limit</param>
        /// <param name="lowerLimit">The lower limit of this channel to be used to calculate time/percentage below the lower limit</param>
        public sVal_MKTStats(int upperLimit, int lowerLimit){
            super(upperLimit, lowerLimit);
            MKTValue = 0;
        }

        /// <summary>The MKT value for the data logged samples</summary>
        public double MKTValue;

        /// <summary>
        /// Update the channel statistics and MKT with a new value read from the data array. To be called every time we read in a new sample.
        /// </summary>
        /// <param name="value">The new data logged value to update the statistics with</param>
        /// <param name="number">The sequence number of the new data logged value</param>
        /// <param name="time">The timestamp of the new data logged value</param>
        public void Update(int value, int number, Date time)
        {
            double kelv = ((value / 10.0) + queryStrings.KELVIN);
            kelv = Math.exp(-DELTAH / (R * kelv));
            MKTValue += kelv;
            //Call the base update to update the inherited base class values
            super.Update(value, number, time);
        }

        /// <summary>
        /// Finalizes the statistics calculations and MKT. To be called at the very end when we finished processing all the samples.
        /// For instance, average can only be calculated from the total sum/total samples so this gets calculated by calling this function.
        /// </summary>
        /// <param name="totalCount">The total number of samples we have read in and wish to calculate statistics for</param>
        /// <param name="samplePeriod">The sample period in seconds to use when calculating time above/below limits</param>
        public void Finalize(int totalCount, int samplePeriod)
        {
            MKTValue = MKTValue / totalCount;
            MKTValue = Math.log(MKTValue) * -1;
            MKTValue = ((DELTAH / R) / MKTValue); //10 / MKTValue;
            MKTValue -= QueryStrings.KELVIN;

            //Call the base finalize to update and finalize the inherited base class values
            super.Finalize(totalCount, samplePeriod);
        }

    }


}
