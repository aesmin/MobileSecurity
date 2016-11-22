package com.ssav.Virusprotection;

/**
 * Created by Minaz on 2/17/14.
 */
public class UpdateReport implements UpdateParameter
{
    String title;
    String updatettime;
    String fromDb;
    String toDb;
    String status;

    public UpdateReport(String title, String updatettime, String fromDb, String toDb, String status)
    {
        this.title=title;
        this.updatettime=updatettime;
        this.fromDb=fromDb;
        this.toDb=toDb;
        this.status=status;
    }



    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getUpdateTime()
    {
        return updatettime;
    }

    @Override
    public String getFromDB()
    {
        return fromDb;
    }

    @Override
    public String getToDB()
    {
        return toDb;
    }

    @Override
    public String getStatus()
    {
        return status;
    }


}