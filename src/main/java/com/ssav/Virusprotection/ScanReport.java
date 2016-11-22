package com.ssav.Virusprotection;



/**
 * Created by Minaz on 2/7/14.
 */

public class ScanReport implements ScanParameter
{
    String title;
    String scansttime;
    String scanendtime;
    String nooffilesscanned;
    String scantime;
    String status;

    public ScanReport(String title, String scansttime, String scanendtime, String nooffilesscanned, String scantime, String status)
    {
        this.title=title;
        this.scansttime=scansttime;
        this.scanendtime=scanendtime;
        this.nooffilesscanned=nooffilesscanned;
        this.scantime=scansttime;
        this.status=status;
    }



    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getScanStartTime()
    {
        return scansttime;
    }

    @Override
    public String getScanEndTime()
    {
        return scanendtime;
    }

    @Override
    public String getNumberOfFilesScanned()
    {
        return nooffilesscanned;
    }

    @Override
    public String getScanTime()
    {
        return scansttime;
    }
    @Override
    public String getStatus()
    {
        return status;
    }

}